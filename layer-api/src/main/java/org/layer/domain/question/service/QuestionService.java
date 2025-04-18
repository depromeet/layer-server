package org.layer.domain.question.service;

import java.util.List;

import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.enums.AnswerStatus;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.question.controller.dto.response.QuestionGetResponse;
import org.layer.domain.question.controller.dto.response.QuestionListGetResponse;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final RetrospectRepository retrospectRepository;
	private final AnswerRepository answerRepository;

	public QuestionListGetResponse getRetrospectQuestions(Long spaceId, Long retrospectId, Long memberId){

		// 해당 멤버가 스페이스 소속인지 검증 로직
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		// 해당 회고가 있는지, PROCEEDING 상태인지 검증
		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospect.isProceedingRetrospect();

		List<Question> questions = questionRepository.findAllByRetrospectIdAndQuestionOwnerOrderByQuestionOrder(
			retrospectId, QuestionOwner.TEAM);

		List<QuestionGetResponse> responses = questions.stream()
			.map(q -> QuestionGetResponse.of(q.getId(), q.getContent(), q.getQuestionOrder(), q.getQuestionType().getStyle()))
			.toList();

		// 임시 저장 여부 확인
		Answers answers = new Answers(
			answerRepository.findAllByRetrospectIdAndMemberIdAndAnswerStatus(retrospectId, memberId, AnswerStatus.TEMPORARY));

		return QuestionListGetResponse.of(responses, answers.hasRetrospectAnswer(memberId, retrospectId));
	}
}
