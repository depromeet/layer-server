package org.layer.admin.template.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.layer.admin.template.controller.dto.TemplateClickCountResponse;
import org.layer.admin.template.entity.AdminTemplateClickHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminTemplateClickHistoryRepository extends JpaRepository<AdminTemplateClickHistory, Long> {
	@Query("SELECT new org.layer.admin.template.controller.dto.TemplateClickCountResponse(v.viewType, COUNT(v)) " +
		"FROM AdminTemplateClickHistory v " +
		"WHERE v.eventTime BETWEEN :startDate AND :endDate " +
		"AND v.memberId NOT IN :excludedIds " +
		"GROUP BY v.viewType ")
	List<TemplateClickCountResponse> countByViewType(
		LocalDateTime startDate,
		LocalDateTime endDate,
		@Param("excludedIds") Collection<Long> excludedIds);
}
