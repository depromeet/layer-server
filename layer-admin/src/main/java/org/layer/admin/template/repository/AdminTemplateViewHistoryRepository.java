package org.layer.admin.template.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.template.controller.dto.TemplateViewCountResponse;
import org.layer.admin.template.entity.AdminTemplateViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminTemplateViewHistoryRepository extends JpaRepository<AdminTemplateViewHistory, Long> {
	@Query("SELECT new org.layer.admin.template.controller.dto.TemplateViewCountResponse(v.viewType, COUNT(v)) " +
		"FROM AdminTemplateViewHistory v " +
		"WHERE v.eventTime BETWEEN :startDate AND :endDate " +
		"GROUP BY v.viewType ")
	List<TemplateViewCountResponse> countByViewType(LocalDateTime startDate, LocalDateTime endDate);
}
