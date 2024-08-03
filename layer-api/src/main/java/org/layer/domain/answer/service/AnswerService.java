package org.layer.domain.answer.service;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.*;

import java.util.List;
import java.util.Optional;

import org.layer.domain.answer.controller.dto.request.AnswerCreateRequest;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.layer.domain.answer.controller.dto.response.TemporaryAnswerGetResponse;
import org.layer.domain.answer.controller.dto.response.TemporaryAnswerListResponse;
import org.layer.domain.answer.entity.Answer;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.enums.AnswerStatus;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.common.time.Time;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.entity.Questions;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.exception.MemberSpaceRelationException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {
	private final AnswerRepository answerRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final RetrospectRepository retrospectRepository;
	private final QuestionRepository questionRepository;

	private final Time time;

	@Transactional
	public void create(AnswerListCreateRequest request, Long spaceId, Long retrospectId, Long memberId) {
		// 스페이스 팀원인지 검증
		Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(
			spaceId, memberId);
		if (team.isEmpty()) {
			throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
		}

		// 회고 존재 검증
		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospect.validateDeadline(time.now());

		// 회고 질문 유효성 검사 - 해당 회고에 속해있는 질문인지
		List<Long> questionIds = request.requests().stream()
			.map(AnswerCreateRequest::questionId)
			.toList();
		Questions questions = new Questions(questionRepository.findAllByIdIn(questionIds));
		questions.validateQuestionSize(questionIds.size());

		// 회고 질문 유효성 검사 - 이미 응답을 하지 않았는지
		Answers answers = new Answers(
			answerRepository.findByRetrospectIdAndMemberIdAndAndAnswerStatusAndQuestionIdIn(retrospectId, memberId, AnswerStatus.DONE, questionIds));
		answers.validateNoAnswer();

		// 기존 임시답변 제거
		answerRepository.deleteAllByRetrospectIdAndMemberIdAndAnswerStatus(retrospectId, memberId,
			AnswerStatus.TEMPORARY);

		AnswerStatus answerStatus = AnswerStatus.DONE;
		if(request.isTemporarySave()){
			answerStatus = AnswerStatus.TEMPORARY;
		}

		for(AnswerCreateRequest r : request.requests()){
			// 회고 질문 유효성 검사 - 각각의 질문들이 유효한지
			questions.validateIdAndQuestionType(r.questionId(), QuestionType.stringToEnum(r.questionType()));

			Answer answer = new Answer(retrospectId, r.questionId(), memberId, r.answer(), answerStatus);
			answerRepository.save(answer);
		}
	}

	public TemporaryAnswerListResponse getTemporaryAnswer(Long spaceId, Long retrospectId, Long memberId){
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospect.isProceedingRetrospect();

		// 임시 답변을 했는지 검증
		Answers answers = new Answers(
			answerRepository.findAllByRetrospectIdAndMemberIdAndAnswerStatus(retrospectId, memberId, AnswerStatus.TEMPORARY));
		answers.validateAlreadyAnswer(memberId, retrospectId);

		// 해당 회고의 모든 질문 조회, 모든 임시답변 조회 -> 질문-임시답변과 매핑
		List<Question> questions = questionRepository.findAllByRetrospectIdAndQuestionOwnerOrderByQuestionOrder(
			retrospectId, QuestionOwner.TEAM);

		List<TemporaryAnswerGetResponse> temporaryAnswers = questions.stream()
			.map(question -> TemporaryAnswerGetResponse.of(question.getId(), question.getQuestionType().getStyle(), answers.getAnswerToQuestion(
				question.getId())))
			.toList();

		return TemporaryAnswerListResponse.of(temporaryAnswers);
	}
}
