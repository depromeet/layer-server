package org.layer.domain.answer.repository;

import java.util.List;
import java.util.Optional;

import org.layer.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	List<Answer> findAllByRetrospectIdIn(List<Long> retrospectIds);

	Optional<Answer> findByRetrospectIdAndQuestionIdAndMemberId(Long retrospectId, Long questionId, Long memberId);
}
