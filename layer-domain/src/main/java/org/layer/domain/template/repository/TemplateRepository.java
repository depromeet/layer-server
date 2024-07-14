package org.layer.domain.template.repository;

import org.layer.domain.template.entity.Template;
import org.layer.domain.template.entity.TemplateQuestion;
import org.layer.domain.template.exception.TemplateException;
import org.layer.domain.template.exception.TemplateExceptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static org.layer.domain.template.exception.TemplateExceptionType.*;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    default Template findByIdOrThrow(Long templateId){
        return findById(templateId)
                .orElseThrow(() -> new TemplateException(TEMPLATE_NOT_FOUND));
    }
}
