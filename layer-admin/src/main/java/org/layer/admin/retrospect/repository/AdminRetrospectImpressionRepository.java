package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.retrospect.entity.AdminRetrospectImpression;
import org.layer.admin.retrospect.repository.dto.ProceedingRetrospectImpressionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRetrospectImpressionRepository extends JpaRepository<AdminRetrospectImpression, Long> {
	@Query("""
		    SELECT new org.layer.admin.retrospect.repository.dto.ProceedingRetrospectImpressionDto(
		        a.memberId,
		        COUNT(a)
		    )
		    FROM AdminRetrospectImpression a
		    WHERE a.eventTime BETWEEN :startDate AND :endDate
		    GROUP BY a.memberId
		""")
	List<ProceedingRetrospectImpressionDto> findProceedingRetrospectImpressionGroupByMember(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);
}
