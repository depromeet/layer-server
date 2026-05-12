package org.layer.admin.space.repository;

import java.time.LocalDateTime;
import java.util.Collection;
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
	WHERE r.createdAt >= :startDate
	  AND r.memberId NOT IN :excludedIds
	GROUP BY r.spaceId
""")
	List<ProceedingSpaceDto> findProceedingSpacesWithMemberCount(
		@Param("startDate") LocalDateTime startDate,
		@Param("excludedIds") Collection<Long> excludedIds
	);

	@Query("SELECT m FROM AdminMemberSpaceRelation m WHERE m.spaceId IN :spaceIds")
	List<AdminMemberSpaceRelation> findAllBySpaceIdIn(@Param("spaceIds") List<Long> spaceIds);
}
