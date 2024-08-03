package org.layer.domain.question.repository;

import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByRetrospectIdAndQuestionOwnerOrderByQuestionOrder(Long retrospectId, QuestionOwner questionOwner);

    List<Question> findAllByRetrospectIdOrderByQuestionOrder(Long retrospectId);

    List<Question> findAllByIdIn(List<Long> questionIds);

    List<Question> findAllByFormId(Long formId);
}
