package org.layer.admin.member.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.member.entity.AdminMemberSignupHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminMemberRepository extends JpaRepository<AdminMemberSignupHistory, Long> {
	List<AdminMemberSignupHistory> findAllByEventTimeBetween(
		LocalDateTime startTime,
		LocalDateTime endTime
	);

	@Query("SELECT a.memberId "
		+ "FROM AdminMemberSignupHistory a "
		+ "WHERE a.eventTime BETWEEN :startTime AND :endTime")
	List<Long> findMemberIdsByEventTimeBetween(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime
	);
}
