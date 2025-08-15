package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.retrospect.entity.AdminRetrospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRetrospectRepository extends JpaRepository<AdminRetrospect, Long> {

	@Query("""
    SELECT DISTINCT r.spaceId
    FROM AdminRetrospect r
    WHERE r.retrospectStatus = 'PROCEEDING'
    AND r.deadline BETWEEN :startDate AND :endDate
""")
	List<Long> findProceedingSpacesByMember(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
