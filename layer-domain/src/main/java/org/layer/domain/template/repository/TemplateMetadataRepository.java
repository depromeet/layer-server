package org.layer.domain.template.repository;

import org.layer.domain.template.entity.TemplateMetadata;
import org.layer.domain.template.exception.TemplateException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.layer.domain.template.exception.TemplateExceptionType.TEMPLATE_NOT_FOUND;

public interface TemplateMetadataRepository extends JpaRepository<TemplateMetadata, Long> {

    Optional<TemplateMetadata> findByFormId(Long formId);
    default TemplateMetadata findByFormIdOrThrow(Long templateId){
        return findByFormId(templateId)
                .orElseThrow(() -> new TemplateException(TEMPLATE_NOT_FOUND));
    }
}
