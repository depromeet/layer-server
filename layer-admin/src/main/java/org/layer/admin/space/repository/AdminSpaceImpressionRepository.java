package org.layer.admin.space.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.space.entity.AdminSpaceImpression;
import org.layer.admin.space.repository.dto.ProceedingSpaceImpressionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSpaceImpressionRepository extends JpaRepository<AdminSpaceImpression, Long> {
	@Query("""
		    SELECT new org.layer.admin.space.repository.dto.ProceedingSpaceImpressionDto(
		        a.memberId,
		        COUNT(a)
		    )
		    FROM AdminSpaceImpression a
		    WHERE a.eventTime BETWEEN :startDate AND :endDate
		    GROUP BY a.memberId
		""")
	List<ProceedingSpaceImpressionDto> findProceedingSpaceImpressionGroupByMember(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);
}
