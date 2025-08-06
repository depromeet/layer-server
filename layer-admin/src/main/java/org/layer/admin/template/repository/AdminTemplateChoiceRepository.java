package org.layer.admin.template.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.template.controller.dto.TemplateChoiceCountResponse;
import org.layer.admin.template.entity.AdminTemplateChoice;
import org.layer.admin.template.enums.AdminChoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminTemplateChoiceRepository extends JpaRepository<AdminTemplateChoice, Long> {

	@Query("SELECT new org.layer.admin.template.controller.dto.TemplateChoiceCountResponse(r.formTag, COUNT(r)) " +
		"FROM AdminTemplateChoice r " +
		"WHERE r.eventTime BETWEEN :startTime AND :endTime " +
		"AND r.choiceType = :choiceType " +
		"GROUP BY r.formTag")
	List<TemplateChoiceCountResponse> countByChoiceType(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime,
		@Param("choiceType") AdminChoiceType choiceType
	);

	@Query("SELECT new org.layer.admin.template.controller.dto.TemplateChoiceCountResponse(r.formTag, COUNT(r)) " +
		"FROM AdminTemplateChoice r " +
		"WHERE r.eventTime BETWEEN :startTime AND :endTime " +
		"GROUP BY r.formTag")
	List<TemplateChoiceCountResponse> countAll(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime
	);
}
