package org.layer.domain.space.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.space.dto.AdminSpaceGetResponse;
import org.layer.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpaceAdminRepository extends JpaRepository<Space, Long> {

	@Query
		("SELECT new org.layer.domain.space.dto.AdminSpaceGetResponse(s.name, m.name) "
		+ "FROM Space s "
		+ "JOIN Member m ON s.leaderId = m.id "
		+ "WHERE s.createdAt >= :startDate "
		+ "AND s.createdAt <= :endDate"
	)
	List<AdminSpaceGetResponse> findAllByCreatedAtAfterAndCreatedAtBefore(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
