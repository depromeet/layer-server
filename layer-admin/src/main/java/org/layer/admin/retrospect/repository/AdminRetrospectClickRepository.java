package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.retrospect.entity.AdminRetrospectClick;
import org.layer.admin.retrospect.repository.dto.ProceedingRetrospectClickDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRetrospectClickRepository extends JpaRepository<AdminRetrospectClick, Long> {

	@Query("""
		SELECT new org.layer.admin.retrospect.repository.dto.ProceedingRetrospectClickDto(
			a.memberId,
			COUNT(a),
			SUM(CASE WHEN a.retrospectStatus = 'PROCEEDING' THEN 1 ELSE 0 END)
		)
		FROM AdminRetrospectClick a
		WHERE a.eventTime BETWEEN :startDate AND :endDate
		GROUP BY a.memberId
		""")
	List<ProceedingRetrospectClickDto> findProceedingRetrospectCTRGroupByMember(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);
}
