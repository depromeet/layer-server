package org.layer.domain.retrospect.repository;

import org.layer.domain.retrospect.dto.AdminRetrospectCountGroupBySpaceGetResponse;
import org.layer.domain.retrospect.dto.AdminRetrospectGetResponse;
import org.layer.domain.retrospect.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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

	// ADMIN 유저가 만든 스페이스에서 진행된 회고 제외
	@Query("SELECT count(*)"
			+ "FROM Retrospect r "
			+ "JOIN Space s ON r.spaceId = s.id "
			+ "JOIN Member m ON s.leaderId = m.id "
			+ "WHERE r.createdAt >= :startDate "
			+ "AND r.createdAt <= :endDate "
			+ "AND m.memberRole = 'USER'"
	)
	Long countRetrospectsExceptForAdminSpace(@Param("startDate") LocalDateTime startDate,
											 @Param("endDate") LocalDateTime endDate);


	@Query("SELECT count(*)"
			+ "FROM Retrospect r "
			+ "JOIN Space s ON r.spaceId = s.id "
			+ "JOIN Member m ON s.leaderId = m.id "
			+ "WHERE r.spaceId = :spaceId "
			+ "AND r.createdAt >= :startDate "
			+ "AND r.createdAt <= :endDate"
	)
	Long countRetrospectsBySpaceId(@Param("spaceId") Long spaceId,
								   @Param("startDate") LocalDateTime startDate,
								   @Param("endDate") LocalDateTime endDate);

	@Query("SELECT new org.layer.domain.retrospect.dto.AdminRetrospectCountGroupBySpaceGetResponse(s, m, COUNT(r)) "
			+ "FROM Retrospect r "
			+ "JOIN Space s ON r.spaceId = s.id "
			+ "JOIN Member m ON s.leaderId = m.id "
			+ "WHERE r.createdAt >= :startDate "
			+ "AND r.createdAt <= :endDate "
			+ "AND m.memberRole = 'USER' "
			+ "GROUP BY s"
	)
	List<AdminRetrospectCountGroupBySpaceGetResponse> countRetrospectsGroupBySpace(@Param("startDate") LocalDateTime startDate,
																			 @Param("endDate") LocalDateTime endDate);
}
