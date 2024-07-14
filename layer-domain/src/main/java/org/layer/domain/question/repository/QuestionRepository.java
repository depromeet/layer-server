package org.layer.domain.question.repository;

import java.util.List;

import org.layer.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findAllByIdIn(List<Long> questionIds);
}
