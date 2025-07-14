package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRetrospectRepository extends JpaRepository<AdminRetrospectAnswerHistory, Long>, AdminCustomRetrospectRepository {

	Optional<AdminRetrospectAnswerHistory> findTopByMemberIdAndSpaceIdAndRetrospectIdOrderByAnswerStartTimeDesc(
		Long memberId, Long spaceId, Long retrospectId);

	List<AdminRetrospectAnswerHistory> findAllByEventTimeBetweenAnd(
		LocalDateTime startTime, LocalDateTime endTime);

	// 엣지 케이스로 답변 종료시간이 없는 경우도 있을 수 있기에 필터링한다.
	List<AdminRetrospectAnswerHistory> findAllByEventTimeBetweenAndAnswerEndTimeIsNotNull(
		LocalDateTime startTime, LocalDateTime endTime);

	void deleteByMemberIdAndSpaceIdAndRetrospectId(
		Long memberId, Long spaceId, Long retrospectId
	);
}
