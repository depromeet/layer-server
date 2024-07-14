package org.layer.domain.template.repository;

import org.layer.domain.template.entity.Template;
import org.layer.domain.template.entity.TemplateQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    //== 질문을 포함한 Template 정보 ==/
    @Query("SELECT tq from TemplateQuestion tq JOIN Template t WHERE t.id = :templateId AND t.id = tq.templateId")
    List<TemplateQuestion> findTemaplteQuestionListById(@Param("templateId") Long templateId);


//    Slice<Template> findAll(Pageable pageable);

}
