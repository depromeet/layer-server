package org.layer.admin.template.repository;

import java.util.List;

import org.layer.admin.template.controller.dto.TemplateRecommendedCountResponse;
import org.layer.admin.template.entity.AdminTemplateRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminTemplateRepository extends JpaRepository<AdminTemplateRecommendation, Long> {

	@Query("SELECT new org.layer.admin.template.controller.dto.TemplateRecommendedCountResponse(r.formTag, COUNT(r)) " +
		"FROM AdminTemplateRecommendation r " +
		"GROUP BY r.formTag")
	List<TemplateRecommendedCountResponse> countByFormTag();
}
