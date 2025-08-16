package org.layer.admin.space.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.space.entity.AdminMemberSpaceRelation;
import org.layer.admin.space.repository.dto.ProceedingSpaceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminMemberSpaceRelationRepository extends JpaRepository<AdminMemberSpaceRelation, Long> {

	@Query("""
		SELECT new org.layer.admin.space.repository.dto.ProceedingSpaceDto(
			r.spaceId,
			COUNT(r.memberId)
		)
		FROM AdminMemberSpaceRelation r
		WHERE r.createdAt BETWEEN :startDate AND :endDate
		GROUP BY r.spaceId
	""")
	List<ProceedingSpaceDto> findProceedingSpacesWithMemberCount(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
