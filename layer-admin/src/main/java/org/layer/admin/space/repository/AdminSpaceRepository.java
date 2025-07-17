package org.layer.admin.space.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.space.controller.dto.SpaceCountResponse;
import org.layer.admin.space.entity.AdminSpaceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSpaceRepository
	extends JpaRepository<AdminSpaceHistory, Long>, AdminSpaceRepositoryCustom {

	@Query("SELECT new org.layer.admin.space.controller.dto.SpaceCountResponse(a.category, COUNT(a)) " +
		"FROM AdminSpaceHistory a " +
		"WHERE a.eventTime BETWEEN :startTime AND :endTime " +
		"GROUP BY a.category")
	List<SpaceCountResponse> findAllByCategory(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime
	);
}
