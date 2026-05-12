package org.layer.admin.space.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.layer.admin.space.controller.dto.SpaceCountResponse;
import org.layer.admin.space.entity.AdminSpaceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSpaceRepository
	extends JpaRepository<AdminSpaceHistory, Long> {

	@Query("SELECT new org.layer.admin.space.controller.dto.SpaceCountResponse(a.category, COUNT(a)) " +
		"FROM AdminSpaceHistory a " +
		"WHERE a.eventTime BETWEEN :startTime AND :endTime " +
		"AND a.memberId NOT IN :excludedIds " +
		"GROUP BY a.category")
	List<SpaceCountResponse> findAllByCategory(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime,
		@Param("excludedIds") Collection<Long> excludedIds
	);

	@Query("SELECT COUNT(a) FROM AdminSpaceHistory a " +
		"WHERE a.eventTime BETWEEN :startTime AND :endTime " +
		"AND a.memberId NOT IN :excludedIds")
	Long countAllByEventTimeBetween(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime,
		@Param("excludedIds") Collection<Long> excludedIds);

	@Query("SELECT COUNT(a) FROM AdminSpaceHistory a WHERE a.memberId NOT IN :excludedIds")
	long countExcluding(@Param("excludedIds") Collection<Long> excludedIds);

	List<AdminSpaceHistory> findAllBySpaceIdIn(Collection<Long> spaceIds);
}
