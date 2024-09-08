package org.layer.domain.space.repository;

import org.layer.domain.space.dto.AdminSpaceGetResponse;
import org.layer.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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


	// ADMIN 유저가 만든 스페이스 제외
	@Query("SELECT count(*)"
					+ "FROM Space s "
					+ "JOIN Member m ON s.leaderId = m.id "
					+ "WHERE s.createdAt >= :startDate "
					+ "AND s.createdAt <= :endDate "
					+ "AND m.memberRole = 'USER'"
	)
	Long countSpacesExceptForAdminSpace(
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate
	);

	// ADMIN 유저가 만든 스페이스에서 진행된 회고 제외
	@Query("SELECT count(*)"
			+ "FROM Retrospect r "
			+ "JOIN Space s ON r.spaceId = s.id "
			+ "JOIN Member m ON s.leaderId = m.id "
			+ "WHERE r.createdAt >= :startDate "
			+ "AND r.createdAt <= :endDate "
			+ "AND m.memberRole = 'USER'"
	)
	Long countRetrospectsExceptForAdminSpace(LocalDateTime startDate, LocalDateTime endDate);
}
