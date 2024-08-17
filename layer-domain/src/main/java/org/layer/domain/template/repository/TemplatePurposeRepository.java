package org.layer.domain.template.repository;

import org.layer.domain.template.entity.TemplatePurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplatePurposeRepository extends JpaRepository<TemplatePurpose, Long> {

    List<TemplatePurpose> findAllByFormId(Long formId);
}
