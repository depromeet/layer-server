package org.layer.admin.member.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.member.entity.AdminMemberSignupHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<AdminMemberSignupHistory, Long> {
	List<AdminMemberSignupHistory> findAllByEventTimeBetween(
		LocalDateTime startTime,
		LocalDateTime endTime
	);

	List<Long> findAllMemberIdsByEventTimeBetween(
		LocalDateTime startTime,
		LocalDateTime endTime
	);
}
