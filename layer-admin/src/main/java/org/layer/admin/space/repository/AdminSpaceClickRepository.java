package org.layer.admin.space.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.space.entity.AdminSpaceClick;
import org.layer.admin.space.repository.dto.ProceedingSpaceClickDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSpaceClickRepository extends JpaRepository<AdminSpaceClick, Long> {

	@Query("""
		    SELECT new org.layer.admin.space.repository.dto.ProceedingSpaceClickDto(
		        a.memberId,
		        COUNT(a),
		        SUM(CASE WHEN a.retrospectStatus = 'PROCEEDING' THEN 1 ELSE 0 END)
		    )
		    FROM AdminSpaceClick a
		    WHERE a.eventTime BETWEEN :startDate AND :endDate
		    GROUP BY a.memberId
		""")
	List<ProceedingSpaceClickDto> findProceedingSpaceClickGroupByMember(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);
}
