package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.layer.admin.retrospect.repository.dto.RetrospectAnswerCompletionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRetrospectAnswerRepository extends JpaRepository<AdminRetrospectAnswerHistory, Long> {

	Optional<AdminRetrospectAnswerHistory> findTopByMemberIdAndSpaceIdAndRetrospectIdOrderByAnswerStartTimeDesc(
		Long memberId, Long spaceId, Long retrospectId);

	@Query("""
		SELECT a.memberId
		FROM AdminRetrospectAnswerHistory a
		WHERE LENGTH(a.answerContent) >= :minLength
		  AND a.eventTime BETWEEN :start AND :end
		  AND a.memberId NOT IN :excludedIds
		GROUP BY a.memberId
		HAVING COUNT(a) >= :minCount
	""")
	List<Long> findMeaningfulMemberIds(
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		@Param("minLength") int minLength,
		@Param("minCount") int minCount,
		@Param("excludedIds") Collection<Long> excludedIds);

	@Query("""
		SELECT new org.layer.admin.retrospect.repository.dto.RetrospectAnswerCompletionDto(
			a.retrospectId,
			MIN(r.targetAnswerCount),
			COUNT(*)
		)
		FROM AdminRetrospectAnswerHistory a
		JOIN AdminRetrospectHistory r ON a.retrospectId = r.retrospectId
		WHERE a.eventTime BETWEEN :start AND :end
		  AND a.memberId NOT IN :excludedIds
		GROUP BY a.retrospectId
	""")
	List<RetrospectAnswerCompletionDto> findRetrospectAnswerCompletionStatsBetween(
		@Param("start") LocalDateTime startTime,
		@Param("end") LocalDateTime endTime,
		@Param("excludedIds") Collection<Long> excludedIds);

	@Query("""
		SELECT COALESCE(AVG(LENGTH(a.answerContent)), 0)
		FROM AdminRetrospectAnswerHistory a
		WHERE a.answerEndTime IS NOT NULL
		  AND a.answerEndTime BETWEEN :startTime AND :endTime
		  AND a.memberId NOT IN :excludedIds
	""")
	double findAverageRetrospectLengthBetween(
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime,
		@Param("excludedIds") Collection<Long> excludedIds
	);

	// 엣지 케이스로 답변 종료시간이 없는 경우도 있을 수 있기에 필터링한다.
	List<AdminRetrospectAnswerHistory> findAllByEventTimeBetweenAndAnswerEndTimeIsNotNull(
		LocalDateTime startTime, LocalDateTime endTime);

	List<AdminRetrospectAnswerHistory> findAllByAnswerEndTimeBetweenAndAnswerStartTimeIsNotNullAndAnswerEndTimeIsNotNull(
		LocalDateTime startTime, LocalDateTime endTime);

	void deleteByMemberIdAndSpaceIdAndRetrospectId(
		Long memberId, Long spaceId, Long retrospectId
	);

	List<AdminRetrospectAnswerHistory> findAllByAnswerEndTimeBetween(
		LocalDateTime startTime, LocalDateTime endTime);

	List<AdminRetrospectAnswerHistory> findAllByAnswerEndTimeBefore(LocalDateTime time);

	@Query("""
		SELECT COUNT(DISTINCT a.retrospectId)
		FROM AdminRetrospectAnswerHistory a
		WHERE a.answerStartTime BETWEEN :start AND :end
		  AND a.memberId NOT IN :excludedIds
	""")
	long countDistinctRetrospectIdByAnswerStartTimeBetween(
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		@Param("excludedIds") Collection<Long> excludedIds);

	@Query("""
		SELECT COUNT(DISTINCT a.retrospectId)
		FROM AdminRetrospectAnswerHistory a
		WHERE a.answerEndTime BETWEEN :start AND :end
		  AND LENGTH(a.answerContent) >= :minLength
		  AND a.memberId NOT IN :excludedIds
	""")
	long countDistinctRetrospectIdByAnswerEndTimeAndQuality(
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		@Param("minLength") int minLength,
		@Param("excludedIds") Collection<Long> excludedIds);

	@Query("""
		SELECT COUNT(DISTINCT a.retrospectId)
		FROM AdminRetrospectAnswerHistory a
		WHERE a.answerEndTime BETWEEN :start AND :end
		  AND a.memberId NOT IN :excludedIds
	""")
	long countDistinctRetrospectIdByAnswerEndTimeBetween(
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		@Param("excludedIds") Collection<Long> excludedIds);

	@Query("SELECT COUNT(a) FROM AdminRetrospectAnswerHistory a WHERE a.memberId NOT IN :excludedIds")
	long countExcluding(@Param("excludedIds") Collection<Long> excludedIds);
}
