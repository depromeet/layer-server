package org.layer.domain.template.repository;

import jakarta.validation.constraints.NotNull;
import org.layer.domain.template.entity.TemplateQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateQuestionRepository extends JpaRepository<TemplateQuestion, Long> {
    List<TemplateQuestion> findByTemplateId(Long templateId);
}
