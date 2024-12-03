package org.layer.domain.answer.repository;

import org.layer.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAnswerRepository extends JpaRepository<Answer, Long> {
	Long countAllByMemberId(Long memberId);
}
