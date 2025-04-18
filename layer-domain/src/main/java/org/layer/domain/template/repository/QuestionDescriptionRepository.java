package org.layer.domain.template.repository;

import org.layer.domain.template.entity.QuestionDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface QuestionDescriptionRepository extends JpaRepository<QuestionDescription, Long> {

    Optional<QuestionDescription> findByQuestionId(Long questionId);
}