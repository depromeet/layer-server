package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AdminRetrospectRepository extends JpaRepository<AdminRetrospectAnswerHistory, Long> {

	Optional<AdminRetrospectAnswerHistory> findTopByMemberIdAndSpaceIdAndRetrospectIdOrderByAnswerStartTimeDesc(
		Long memberId, Long spaceId, Long retrospectId);

	List<AdminRetrospectAnswerHistory> findAllByEventTimeBetween(
		LocalDateTime startTime, LocalDateTime endTime);

	void deleteByMemberIdAndSpaceIdAndRetrospectId(
		Long memberId, Long spaceId, Long retrospectId
	);
}
