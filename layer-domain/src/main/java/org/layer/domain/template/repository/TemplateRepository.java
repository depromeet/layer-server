package org.layer.domain.template.repository;

import org.layer.domain.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    // 간단 정보 단건 조회

}
