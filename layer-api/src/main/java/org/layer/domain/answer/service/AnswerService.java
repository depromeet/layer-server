package org.layer.domain.answer.service;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.*;

import java.util.List;
import java.util.Optional;

import org.layer.domain.answer.entity.Answer;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.answer.service.dto.request.AnswerCreateServiceRequest;
import org.layer.domain.answer.service.dto.request.AnswerListCreateServiceRequest;
import org.layer.domain.common.time.Time;
import org.layer.domain.question.entity.Questions;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
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

	public void create(AnswerListCreateServiceRequest request, Long spaceId, Long retrospectId, Long memberId) {
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
			.map(AnswerCreateServiceRequest::questionId)
			.toList();
		Questions questions = new Questions(questionRepository.findAllByIdIn(questionIds));

		questions.validateQuestionSize(questionIds.size());

		// 회고 질문 유효성 검사 - 이미 응답을 하지 않았는지
		Answers answers = new Answers(
			answerRepository.findByRetrospectIdAndMemberIdAndQuestionIdIn(retrospectId, memberId, questionIds));
		answers.validateNoAnswer();

		request.requests().forEach(
				r -> {
					// 회고 질문 유효성 검사 - 각각의 질문들이 유효한지
					questions.validateIdAndQuestionType(r.questionId(), QuestionType.stringToEnum(r.questionType()));
					Answer answer = new Answer(retrospectId, r.questionId(), memberId, r.answer());
					answerRepository.save(answer);
				});



	}
}
