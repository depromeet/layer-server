package org.layer.admin.retrospect.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.admin.retrospect.controller.dto.MeaningfulRetrospectMemberResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectRetentionResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectStayTimeResponse;
import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.layer.admin.retrospect.entity.AdminRetrospectHistory;
import org.layer.admin.retrospect.enums.AnswerTimeRange;
import org.layer.admin.retrospect.repository.AdminRetrospectAnswerRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectRepository;
import org.layer.event.retrospect.CreateRetrospectEvent;
import org.layer.event.retrospect.AnswerRetrospectEndEvent;
import org.layer.event.retrospect.AnswerRetrospectStartEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminRetrospectService {

	private final AdminRetrospectRepository adminRetrospectRepository;
	private final AdminRetrospectAnswerRepository adminRetrospectAnswerRepository;
	private final AdminMemberRepository adminMemberRepository;

	public MeaningfulRetrospectMemberResponse getAllMeaningfulRetrospect(
		LocalDateTime startTime, LocalDateTime endTime, int retrospectLength, int retrospectCount) {
		List<Long> meaningfulMemberIds = adminRetrospectAnswerRepository.findMeaningfulMemberIds(
			startTime, endTime, retrospectLength, retrospectCount);

		long totalMemberCount = adminMemberRepository.count();

		return new MeaningfulRetrospectMemberResponse(meaningfulMemberIds.size(), totalMemberCount);
	}

	public List<RetrospectStayTimeResponse> getAllRetrospectStayTime(
		LocalDateTime startTime, LocalDateTime endTime) {
		List<AdminRetrospectAnswerHistory> retrospectAnswerHistories = adminRetrospectAnswerRepository.findAllByEventTimeBetweenAndAnswerEndTimeIsNotNull(
			startTime, endTime);

		Map<AnswerTimeRange, Long> countMap = new HashMap<>();
		retrospectAnswerHistories.forEach(history -> {
			AnswerTimeRange range = AnswerTimeRange.from(history.getAnswerTime());
			countMap.put(range, countMap.getOrDefault(range, 0L) + 1);
		});

		List<RetrospectStayTimeResponse> response = new ArrayList<>();
		for (AnswerTimeRange range : AnswerTimeRange.values()) {
			Long count = countMap.getOrDefault(range, 0L);
			response.add(new RetrospectStayTimeResponse(range.getLabel(), count));
		}

		return response;
	}

	public RetrospectRetentionResponse getRetrospectRetention(LocalDateTime startTime, LocalDateTime endTime) {
		List<AdminRetrospectHistory> histories = adminRetrospectRepository.findAllByEventTimeBetween(
			startTime, endTime);

		Map<Long, Long> retrospectCountMap = new HashMap<>();
		histories.forEach(history ->
			retrospectCountMap.merge(history.getMemberId(), 1L, Long::sum)
		);

		// 신규 가입자 목록 조회
		Set<Long> newMemberIdSet = new HashSet<>(
			adminMemberRepository.findAllMemberIdsByEventTimeBetween(startTime, endTime));

		// 리텐션 유저 추출
		List<Long> retainedMemberIds = retrospectCountMap.entrySet().stream()
			.filter(entry -> {
				Long memberId = entry.getKey();
				Long count = entry.getValue();
				// 신규 유저는 첫 회고 제외
				if (newMemberIdSet.contains(memberId)) {
					return count - 1 >= 1;
				}
				// 기존 유저는 1회 이상이면 리텐션
				return count >= 1;
			})
			.map(Map.Entry::getKey)
			.toList();

		// 전체 가입자 수 조회
		long totalMemberCount = adminMemberRepository.count();

		// 평균 리텐션 기간 계산
		long avgRetentionGapSeconds = calculateAverageMinGapInSeconds(histories);

		return new RetrospectRetentionResponse(avgRetentionGapSeconds, retainedMemberIds.size(), totalMemberCount);
	}

	private long calculateAverageMinGapInSeconds(List<AdminRetrospectHistory> histories) {
		// 1. memberId별로 묶음
		Map<Long, List<AdminRetrospectHistory>> grouped = histories.stream()
			.collect(Collectors.groupingBy(AdminRetrospectHistory::getMemberId));

		// 2. 각 memberId마다 최소 시간차(초)를 구함
		List<Long> minGapsPerMember = grouped.values().stream()
			.map(memberHistories -> {
				List<LocalDateTime> sortedTimes = memberHistories.stream()
					.map(AdminRetrospectHistory::getEventTime)
					.sorted()
					.toList();

				if (sortedTimes.size() < 2)
					return null;

				List<Long> gaps = new ArrayList<>();
				for (int i = 1; i < sortedTimes.size(); i++) {
					long seconds = Duration.between(sortedTimes.get(i - 1), sortedTimes.get(i)).getSeconds();
					gaps.add(seconds);
				}
				return gaps.stream().min(Long::compareTo).orElse(null);
			})
			.filter(Objects::nonNull) // eventTime이 2개 이상인 멤버만 포함
			.toList();

		// 3. 전체 평균 계산 (소수점 버리고 long 반환)
		return (long)minGapsPerMember.stream()
			.mapToLong(Long::longValue)
			.average()
			.orElse(0.0);  // 데이터가 없을 경우 0

	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveRetrospectAnswerHistory(AnswerRetrospectStartEvent event) {
		adminRetrospectAnswerRepository.deleteByMemberIdAndSpaceIdAndRetrospectId(event.memberId(), event.spaceId(),
			event.retrospectId());

		AdminRetrospectAnswerHistory retrospectAnswerHistory = AdminRetrospectAnswerHistory.builder()
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.spaceId(event.spaceId())
			.retrospectId(event.retrospectId())
			.answerStartTime(event.eventTime())
			.build();

		adminRetrospectAnswerRepository.save(retrospectAnswerHistory);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void updateRetrospectAnswerHistory(AnswerRetrospectEndEvent event) {

		adminRetrospectAnswerRepository.findTopByMemberIdAndSpaceIdAndRetrospectIdOrderByAnswerStartTimeDesc(
				event.memberId(), event.spaceId(), event.retrospectId())
			.ifPresentOrElse(
				history -> {
					history.updateRetrospectCompleted(event.eventTime(), event.answerContent());
					adminRetrospectAnswerRepository.save(history);
				},
				() -> {
					AdminRetrospectAnswerHistory retrospectAnswerHistory = AdminRetrospectAnswerHistory.builder()
						.eventTime(event.eventTime())
						.memberId(event.memberId())
						.eventId(event.eventId())
						.spaceId(event.spaceId())
						.retrospectId(event.retrospectId())
						.answerEndTime(event.eventTime())
						.answerContent(event.answerContent())
						.build();
					adminRetrospectAnswerRepository.save(retrospectAnswerHistory);
				}
			);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveRetrospectHistory(CreateRetrospectEvent event) {
		AdminRetrospectHistory retrospectHistory = AdminRetrospectHistory.builder()
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.spaceId(event.spaceId())
			.retrospectId(event.retrospectId())
			.targetAnswerCount(event.targetAnswerCount())
			.build();

		adminRetrospectRepository.save(retrospectHistory);
	}
}
