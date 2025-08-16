package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.retrospect.entity.AdminRetrospectHistory;
import org.layer.admin.retrospect.repository.dto.SpaceRetrospectCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRetrospectHistoryRepository extends JpaRepository<AdminRetrospectHistory, Long> {
	List<AdminRetrospectHistory> findAllByEventTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

	List<AdminRetrospectHistory> findAllByEventTimeBefore(LocalDateTime time);

	@Query("SELECT new org.layer.admin.retrospect.repository.dto.SpaceRetrospectCountDto(m.spaceId, COUNT(m)) " +
		"FROM AdminRetrospectHistory m " +
		"WHERE m.eventTime BETWEEN :startTime AND :endTime " +
		"GROUP BY m.spaceId ")
	List<SpaceRetrospectCountDto> findRetrospectCountGroupedBySpaceWithPeriod(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime
	);



}
