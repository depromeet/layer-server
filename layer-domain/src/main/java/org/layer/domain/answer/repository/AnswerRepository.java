package org.layer.domain.answer.repository;

import java.util.List;

import org.layer.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	List<Answer> findAllByRetrospectIdIn(List<Long> retrospectIds);

	List<Answer> findByRetrospectIdAndMemberIdAndQuestionIdIn(Long retrospectId, Long memberId, List<Long> questionId);
}
