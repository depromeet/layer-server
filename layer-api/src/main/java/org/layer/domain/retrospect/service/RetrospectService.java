package org.layer.domain.retrospect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.layer.domain.retrospect.dto.SpaceMemberCount;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.event.ai.AIAnalyzeStartEvent;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.common.random.CustomRandom;
import org.layer.domain.common.time.Time;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.entity.FormType;
import org.layer.domain.form.enums.FormTag;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.controller.dto.request.QuestionCreateRequest;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.controller.dto.request.RetrospectUpdateRequest;
import org.layer.domain.retrospect.controller.dto.response.RetrospectGetResponse;
import org.layer.domain.retrospect.controller.dto.response.RetrospectListGetResponse;
import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.event.retrospect.CreateRetrospectEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RetrospectService {

	private final RetrospectRepository retrospectRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final FormRepository formRepository;
	private final SpaceRepository spaceRepository;

	private final ApplicationEventPublisher eventPublisher;

	private final CustomRandom random;
	private final Time time;

	@Transactional
	public Long createRetrospect(RetrospectCreateRequest request, Long spaceId, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		Retrospect retrospect = getRetrospect(request, spaceId);
		Retrospect savedRetrospect = retrospectRepository.save(retrospect);

		List<Question> questions = getQuestions(request.questions(), savedRetrospect.getId(), null);
		questionRepository.saveAll(questions);

		publishCreateRetrospectEvent(spaceId, retrospect.getId(), team.getTeamMemberCount(),
			retrospect.getTitle(), memberId);

		Space space = spaceRepository.findByIdOrThrow(spaceId);

		// 새로운 폼 생성(수정)인지 확인
		if (Boolean.FALSE.equals(request.isNewForm())) {
			space.updateRecentFormId(request.curFormId(), memberId);
			return savedRetrospect.getId();
		}

		// 내 회고 폼에 추가
		FormTag formTag = FormTag.CUSTOM;
		if (!request.hasChangedOriginal()) {
			Form prevForm = formRepository.findByIdOrThrow(request.curFormId());
			formTag = prevForm.getFormTag();
		}

		Form form = new Form(memberId, spaceId, request.formName(), request.introduction(), FormType.CUSTOM, formTag);
		Form savedForm = formRepository.save(form);

		List<Question> myQuestions = getQuestions(request.questions(), null, savedForm.getId());
		questionRepository.saveAll(myQuestions);

		// 스페이스 최근 폼 수정
		space.updateRecentFormId(savedForm.getId(), memberId);
		return savedRetrospect.getId();
	}

	private Retrospect getRetrospect(RetrospectCreateRequest request, Long spaceId) {
		return Retrospect.builder()
			.spaceId(spaceId)
			.title(request.title())
			.introduction(request.introduction())
			.retrospectStatus(RetrospectStatus.PROCEEDING)
			.analysisStatus(AnalysisStatus.NOT_STARTED)
			.deadline(request.deadline())
			.build();
	}

	private void publishCreateRetrospectEvent(Long spaceId, Long retrospectId, long targetAnswerCount,
		String title, Long memberId) {
		eventPublisher.publishEvent(CreateRetrospectEvent.of(
			random.generateRandomValue(),
			spaceId,
			retrospectId,
			targetAnswerCount,
			title,
			memberId,
			time.now()
		));
	}

	public RetrospectListGetResponse getRetrospects(Long spaceId, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		List<Retrospect> retrospects = retrospectRepository.findAllBySpaceId(spaceId);
		List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
		Answers answers = new Answers(answerRepository.findAllByRetrospectIdIn(retrospectIds));

		List<RetrospectGetResponse> retrospectDtos = retrospects.stream()
			.map(r -> {
				long writeCount = team.getTeamMemberCount();
				if (r.getRetrospectStatus().equals(RetrospectStatus.DONE)) {
					writeCount = answers.getWriteCount(r.getId());
				}

				return RetrospectGetResponse.of(r.getSpaceId(), r.getId(), r.getTitle(), r.getIntroduction(),
					answers.getWriteStatus(memberId, r.getId()), r.getRetrospectStatus(), r.getAnalysisStatus(),
					answers.getWriteCount(r.getId()), writeCount, r.getCreatedAt(), r.getDeadline());
			})
			.toList();

		return RetrospectListGetResponse.of(retrospects.size(), retrospectDtos);
	}

	public RetrospectListGetResponse getAllRetrospects(Long memberId) {

		List<MemberSpaceRelation> msrList = memberSpaceRelationRepository.findAllByMemberId(memberId);
		List<Long> spaceIds = msrList.stream().map(m -> m.getSpace().getId()).toList();

		Map<Long, Long> spaceMemberCountMap = memberSpaceRelationRepository.countMembersBySpaceIds(spaceIds)
			.stream().collect(Collectors.toMap(SpaceMemberCount::getSpaceId, SpaceMemberCount::getMemberCount));

		List<Retrospect> retrospects = retrospectRepository.findAllBySpaceIdIn(spaceIds);
		List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
		Answers answers = new Answers(answerRepository.findAllByRetrospectIdIn(retrospectIds));

		List<RetrospectGetResponse> retrospectDtos = retrospects.stream()
			.map(r -> {
				long writeCount = spaceMemberCountMap.get(r.getSpaceId());
				if (r.getRetrospectStatus().equals(RetrospectStatus.DONE)) {
					writeCount = answers.getWriteCount(r.getId());
				}

				return RetrospectGetResponse.of(r.getSpaceId(), r.getId(), r.getTitle(), r.getIntroduction(),
					answers.getWriteStatus(memberId, r.getId()), r.getRetrospectStatus(), r.getAnalysisStatus(),
					answers.getWriteCount(r.getId()), writeCount, r.getCreatedAt(), r.getDeadline());
			})
			.toList();

		return RetrospectListGetResponse.of(retrospects.size(), retrospectDtos);
	}

	private List<Question> getQuestions(List<QuestionCreateRequest> questions, Long savedRetrospectId, Long formId) {
		AtomicInteger index = new AtomicInteger(1);

		return questions.stream()
			.map(question -> Question.builder()
				.retrospectId(savedRetrospectId)
				.formId(formId)
				.content(question.questionContent())
				.questionOrder(index.getAndIncrement())
				.questionOwner(QuestionOwner.TEAM)
				.questionType(QuestionType.stringToEnum(question.questionType()))
				.build())
			.toList();
	}

	@Transactional
	public void updateRetrospect(Long spaceId, Long retrospectId, RetrospectUpdateRequest request, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		// 팀장인지 검증
		Space space = spaceRepository.findByIdOrThrow(spaceId);
		space.isLeaderSpace(memberId);

		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospect.updateRetrospect(request.title(), request.introduction(), request.deadline(), time);
	}

	@Transactional
	public void deleteRetrospect(Long spaceId, Long retrospectId, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		// 팀장인지 검증
		Space space = spaceRepository.findByIdOrThrow(spaceId);
		space.isLeaderSpace(memberId);

		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospectRepository.delete(retrospect);
	}

	@Transactional
	public void closeRetrospect(Long spaceId, Long retrospectId, Long memberId) {
		// 팀장인지 검증
		Space space = spaceRepository.findByIdOrThrow(spaceId);
		space.isLeaderSpace(memberId);

		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);

		retrospect.completeRetrospectAndStartAnalysis(time.now());
		if (retrospect.getAnalysisStatus().equals(AnalysisStatus.DONE)) {
			log.error("비정상적인 오류입니다.");
			return;
		}
		retrospectRepository.save(retrospect);

		// 회고 ai 분석 시작
		eventPublisher.publishEvent(AIAnalyzeStartEvent.of(retrospectId));
	}
}
