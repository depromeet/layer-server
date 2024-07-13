package org.layer.domain.retrospect.service;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.*;

import java.util.List;
import java.util.Optional;

import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.retrospect.service.dto.request.RetrospectCreateServiceRequest;
import org.layer.domain.retrospect.service.dto.response.RetrospectGetServiceResponse;
import org.layer.domain.retrospect.service.dto.response.RetrospectListGetServiceResponse;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.exception.MemberSpaceRelationException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetrospectService {

	private final RetrospectRepository retrospectRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final FormRepository formRepository;

	@Transactional
	public void createRetrospect(RetrospectCreateServiceRequest request, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		validateTeamMember(request.spaceId(), memberId);

		Retrospect retrospect = getRetrospect(request);
		Retrospect savedRetrospect = retrospectRepository.save(retrospect);

		List<Question> questions = request.questions().stream()
			.map(q -> new Question(savedRetrospect.getId(), q, QuestionOwner.TEAM, QuestionType.PLAIN_TEXT))
			.toList();
		questionRepository.saveAll(questions);

		if(request.isMyForm()){
			Form form = new Form(memberId, request.title(), request.introduction());
			Form savedForm = formRepository.save(form);

			List<Question> myQuestions = request.questions().stream()
				.map(q -> new Question(savedForm.getId(), q, QuestionOwner.INDIVIDUAL, QuestionType.PLAIN_TEXT))
				.toList();
			questionRepository.saveAll(myQuestions);
		}
	}

	private Retrospect getRetrospect(RetrospectCreateServiceRequest request) {
		return Retrospect.builder()
			.title(request.title())
			.spaceId(request.spaceId())
			.introduction(request.introduction())
			.retrospectStatus(RetrospectStatus.PROCEEDING)
			.build();
	}

	public RetrospectListGetServiceResponse getRetrospects(Long spaceId, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		validateTeamMember(spaceId, memberId);

		List<Retrospect> retrospects = retrospectRepository.findAllBySpaceId(spaceId);
		List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
		Answers answers = new Answers(answerRepository.findAllByRetrospectIdIn(retrospectIds));

		List<RetrospectGetServiceResponse> retrospectDtos = retrospects.stream()
			.map(r -> RetrospectGetServiceResponse.of(r.getTitle(), r.getIntroduction(),
				answers.hasRetrospectAnswer(memberId), r.getRetrospectStatus(), answers.getWriteCount()))
			.toList();

		return RetrospectListGetServiceResponse.of(retrospects.size(), retrospectDtos);
	}

	private void validateTeamMember(Long request, Long memberId) {
		Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(
			request, memberId);
		if (team.isEmpty()) {
			throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
		}
	}
}
