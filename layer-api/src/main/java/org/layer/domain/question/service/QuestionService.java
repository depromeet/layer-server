package org.layer.domain.question.service;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.*;

import java.util.List;
import java.util.Optional;

import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.question.service.dto.response.QuestionGetServiceResponse;
import org.layer.domain.question.service.dto.response.QuestionListGetServiceResponse;
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
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final RetrospectRepository retrospectRepository;

	public QuestionListGetServiceResponse getRetrospectQuestions(Long spaceId, Long retrospectId, Long memberId){

		// 해당 멤버가 스페이스 소속인지 검증 로직
		Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(
			spaceId, memberId);
		if(team.isEmpty()){
			throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
		}

		// 해당 회고가 있는지, PROCEEDING 상태인지 검증
		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospect.isProceedingRetrospect();

		List<Question> questions = questionRepository.findAllByQuestionOwnerIdAndQuestionOwnerOrderByQuestionOrder(
			retrospectId, QuestionOwner.TEAM);

		List<QuestionGetServiceResponse> serviceResponses = questions.stream()
			.map(q -> QuestionGetServiceResponse.of(q.getQuestionOwnerId(), q.getContent(), q.getQuestionOrder(), q.getQuestionType().getStyle()))
			.toList();

		return QuestionListGetServiceResponse.of(serviceResponses);
	}
}
