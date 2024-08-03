package org.layer.domain.template.repository;

import org.layer.domain.template.entity.QuestionDescription;
import org.layer.domain.template.exception.TemplateException;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import static org.layer.common.exception.QuestionExceptionType.DESCRIPTION_NOT_FOUND;


public interface QuestionDescriptionRepository extends JpaRepository<QuestionDescription, Long> {

    Optional<QuestionDescription> findByQuestionId(Long questionId);
    default QuestionDescription findByQuestionIdOrThrow(Long questionId){
        return findByQuestionId(questionId)
                .orElseThrow(() -> new TemplateException(DESCRIPTION_NOT_FOUND));
    }
}