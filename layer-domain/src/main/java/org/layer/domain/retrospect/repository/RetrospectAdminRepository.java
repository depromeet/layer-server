package org.layer.domain.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.retrospect.dto.AdminRetrospectGetResponse;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.dto.AdminSpaceGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RetrospectAdminRepository extends JpaRepository<Retrospect, Long> {

	@Query
		("SELECT new org.layer.domain.retrospect.dto.AdminRetrospectGetResponse(r.title, m.name) "
			+ "FROM Retrospect r "
			+ "JOIN Space s ON r.spaceId = s.id "
			+ "JOIN Member m ON s.leaderId = m.id "
			+ "WHERE s.createdAt >= :startDate "
			+ "AND s.createdAt <= :endDate"
		)
	List<AdminRetrospectGetResponse> findAllByCreatedAtAfterAndCreatedAtBefore(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);
}
