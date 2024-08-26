package org.layer.domain.retrospect.service;

import static org.layer.common.exception.RetrospectExceptionType.*;

import lombok.RequiredArgsConstructor;

import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
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
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.exception.RetrospectException;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.retrospect.service.dto.response.RetrospectGetServiceResponse;
import org.layer.domain.retrospect.service.dto.response.RetrospectListGetServiceResponse;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetrospectService {

	private final RetrospectRepository retrospectRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final FormRepository formRepository;
	private final SpaceRepository spaceRepository;

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

		Space space = spaceRepository.findByIdOrThrow(spaceId);

		// 새로운 폼 생성(수정)인지 확인
		if (!request.isNewForm()) {
			space.updateRecentFormId(request.curFormId(), memberId);
			return savedRetrospect.getId();
		}

		// 내 회고 폼에 추가
		Form form = new Form(memberId, spaceId, request.formName(), request.introduction(), FormType.CUSTOM,
			FormTag.CUSTOM);
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
			.deadline(request.deadline())
			.build();
	}

	public RetrospectListGetServiceResponse getRetrospects(Long spaceId, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		List<Retrospect> retrospects = retrospectRepository.findAllBySpaceId(spaceId);
		List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
		Answers answers = new Answers(answerRepository.findAllByRetrospectIdIn(retrospectIds));

		List<RetrospectGetServiceResponse> retrospectDtos = retrospects.stream()
			.map(r -> RetrospectGetServiceResponse.of(r.getId(), r.getTitle(), r.getIntroduction(),
				answers.hasRetrospectAnswer(memberId, r.getId()), r.getRetrospectStatus(),
				answers.getWriteCount(), team.getTeamMemberCount(), r.getCreatedAt(), r.getDeadline()))
			.toList();

		return RetrospectListGetServiceResponse.of(retrospects.size(), retrospectDtos);
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
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		// 팀장인지 검증
		Space space = spaceRepository.findByIdOrThrow(spaceId);
		space.isLeaderSpace(memberId);

		// 팀원 모두 회고를 작성했는지 검증
		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);

		Answers answers = new Answers(answerRepository.findAllByRetrospectId(retrospectId));

		if (answers.getWriteCount() != team.getTeamMemberCount()) {
			throw new RetrospectException(NOT_COMPLETE_RETROSPECT_MEMBER);
		}

		retrospect.updateRetrospectStatus(RetrospectStatus.DONE);
	}
}
