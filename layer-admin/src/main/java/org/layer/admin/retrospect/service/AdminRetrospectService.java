package org.layer.admin.retrospect.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.admin.retrospect.controller.dto.CumulativeRetrospectCountResponse;
import org.layer.admin.retrospect.controller.dto.MeaningfulRetrospectMemberResponse;
import org.layer.admin.retrospect.controller.dto.ProceedingRetrospectCTRAverageResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectCompletionRateResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectRetentionResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectStayTimeResponse;
import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.layer.admin.retrospect.entity.AdminRetrospectHistory;
import org.layer.admin.retrospect.entity.AdminRetrospectClick;
import org.layer.admin.retrospect.entity.AdminRetrospectImpression;
import org.layer.admin.retrospect.enums.AdminRetrospectStatus;
import org.layer.admin.retrospect.enums.AnswerTimeRange;
import org.layer.admin.retrospect.repository.AdminRetrospectAnswerRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectClickRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectImpressionRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectRepository;
import org.layer.admin.retrospect.repository.dto.ProceedingRetrospectClickDto;
import org.layer.admin.retrospect.repository.dto.ProceedingRetrospectImpressionDto;
import org.layer.admin.retrospect.repository.dto.RetrospectAnswerCompletionDto;
import org.layer.admin.retrospect.repository.dto.SpaceRetrospectCountDto;
import org.layer.admin.space.controller.dto.ProceedingSpaceCTRAverageResponse;
import org.layer.admin.space.repository.AdminSpaceRepository;
import org.layer.admin.space.repository.dto.ProceedingSpaceImpressionDto;
import org.layer.event.retrospect.ClickRetrospectEvent;
import org.layer.event.retrospect.CreateRetrospectEvent;
import org.layer.event.retrospect.AnswerRetrospectEndEvent;
import org.layer.event.retrospect.AnswerRetrospectStartEvent;
import org.layer.event.retrospect.ImpressionRetrospectEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminRetrospectService {

	private final AdminRetrospectRepository adminRetrospectRepository;
	private final AdminRetrospectAnswerRepository adminRetrospectAnswerRepository;
	private final AdminRetrospectImpressionRepository adminRetrospectImpressionRepository;
	private final AdminRetrospectClickRepository adminRetrospectClickRepository;
	private final AdminMemberRepository adminMemberRepository;
	private final AdminSpaceRepository adminSpaceRepository;

	public MeaningfulRetrospectMemberResponse getAllMeaningfulRetrospect(
		LocalDateTime startTime, LocalDateTime endTime, int retrospectLength, int retrospectCount) {
		List<Long> meaningfulMemberIds = adminRetrospectAnswerRepository.findMeaningfulMemberIds(
			startTime, endTime, retrospectLength, retrospectCount);

		long totalMemberCount = adminMemberRepository.findAllByEventTimeBefore(endTime).size();

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
		List<AdminRetrospectHistory> prevHistories = adminRetrospectRepository.findAllByEventTimeBefore(startTime);

		Map<Long, Long> retrospectCountMap = new HashMap<>();
		histories.forEach(history ->
			retrospectCountMap.merge(history.getMemberId(), 1L, Long::sum)
		);

		// 신규 가입자 목록 조회
		Set<Long> newMemberIdSet = new HashSet<>(
			adminMemberRepository.findMemberIdsByEventTimeBetween(startTime, endTime));

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
		long totalMemberCount = adminMemberRepository.findAllByEventTimeBefore(endTime).size();

		// 평균 리텐션 기간 계산
		long avgRetentionGapSeconds = calculateAverageMinGapInSeconds(histories, prevHistories);

		return new RetrospectRetentionResponse(avgRetentionGapSeconds, retainedMemberIds.size(), totalMemberCount);
	}

	private long calculateAverageMinGapInSeconds(List<AdminRetrospectHistory> histories,
		List<AdminRetrospectHistory> prevHistories) {
		// 1. memberId별로 묶음
		Map<Long, List<AdminRetrospectHistory>> grouped = histories.stream()
			.collect(Collectors.groupingBy(AdminRetrospectHistory::getMemberId));

		// 2. prevHistories를 memberId별 최신 eventTime으로 매핑
		Map<Long, LocalDateTime> prevLatestMap = prevHistories.stream()
			.collect(Collectors.groupingBy(
				AdminRetrospectHistory::getMemberId,
				Collectors.collectingAndThen(
					Collectors.maxBy(Comparator.comparing(AdminRetrospectHistory::getEventTime)),
					opt -> opt.map(AdminRetrospectHistory::getEventTime).orElse(null)
				)
			));

		// 2. 각 memberId마다 최소 시간차(초)를 구함
		List<Long> minGapsPerMember = grouped.entrySet().stream()
			.map(entry -> {
				Long memberId = entry.getKey();
				List<LocalDateTime> sortedTimes = entry.getValue().stream()
					.map(AdminRetrospectHistory::getEventTime)
					.sorted()
					.toList();

				List<Long> gaps = new ArrayList<>();

				// prevHistories와 비교할 수 있는 시점이 있다면 추가
				if (prevLatestMap.containsKey(memberId)) {
					LocalDateTime prevLatest = prevLatestMap.get(memberId);
					long seconds = Duration.between(prevLatest, sortedTimes.get(0)).getSeconds();
					gaps.add(seconds);
				}

				// 현재 리스트 내에서 시간차 추가
				for (int i = 1; i < sortedTimes.size(); i++) {
					long seconds = Duration.between(sortedTimes.get(i - 1), sortedTimes.get(i)).getSeconds();
					gaps.add(seconds);
				}

				// 최소 gap 반환
				return gaps.isEmpty() ? null : Collections.min(gaps);
			})
			.filter(Objects::nonNull)
			.toList();

		// 3. 전체 평균 계산 (소수점 버리고 long 반환)
		return (long)minGapsPerMember.stream()
			.mapToLong(Long::longValue)
			.average()
			.orElse(0.0);  // 데이터가 없을 경우 0
	}

	public CumulativeRetrospectCountResponse getCumulativeRetrospectCount(
		LocalDateTime startTime, LocalDateTime endTime) {

		List<SpaceRetrospectCountDto> histories = adminRetrospectRepository.findRetrospectCountGroupedBySpaceWithPeriod(startTime,
			endTime);

		if (histories.isEmpty()) {
			return new CumulativeRetrospectCountResponse(0L); // 또는 null/Optional 등으로 처리 가능
		}

		long totalRetrospectCount = histories.stream()
			.mapToLong(SpaceRetrospectCountDto::count)
			.sum();

		Long totalSpaceCount = adminSpaceRepository.countAllByEventTimeBetween(startTime, endTime);
		double averageCumulativeCount = totalSpaceCount == 0 ? 0.0 : (double)totalRetrospectCount / totalSpaceCount;
		return new CumulativeRetrospectCountResponse(averageCumulativeCount);
	}

	public RetrospectCompletionRateResponse getRetrospectCompletionRate(LocalDateTime startTime, LocalDateTime endTime) {
		List<RetrospectAnswerCompletionDto> answerHistories = adminRetrospectAnswerRepository.findRetrospectAnswerCompletionStatsBetween(
			startTime, endTime);

		// 회고별 완수율 계산 (단위: %)
		List<Double> completionRates = answerHistories.stream()
			.filter(dto -> dto.targetAnswerCount() > 0) // division by zero 방지
			.map(dto -> (double) dto.actualAnswerCount() / dto.targetAnswerCount() * 100.0)
			.toList();

		// 평균 완수율 계산
		double averageCompletionRate = completionRates.isEmpty()
			? 0.0
			: completionRates.stream()
			.mapToDouble(Double::doubleValue)
			.average()
			.orElse(0.0);

		return new RetrospectCompletionRateResponse(averageCompletionRate);
	}

	public ProceedingRetrospectCTRAverageResponse getProceedingRetrospectCTR(LocalDateTime startDate, LocalDateTime endDate) {
		List<ProceedingRetrospectImpressionDto> impressions = adminRetrospectImpressionRepository.findProceedingRetrospectImpressionGroupByMember(
			startDate, endDate);
		List<ProceedingRetrospectClickDto> clicks = adminRetrospectClickRepository.findProceedingRetrospectCTRGroupByMember(
			startDate, endDate);

		Map<Long, Long> impressionMap = impressions.stream()
			.collect(Collectors.toMap(
				ProceedingRetrospectImpressionDto::memberId,
				ProceedingRetrospectImpressionDto::totalCount
			));

		// 각 멤버의 CTR 리스트
		List<Double> ctrList = clicks.stream()
			.map(clickDto -> {
				Long memberId = clickDto.memberId();
				Long impressionCount = impressionMap.getOrDefault(memberId, 0L);

				if (impressionCount == 0) {
					return null; // 나눌 수 없으면 제외
				}

				return clickDto.proceedingCount() / (double) impressionCount;
			})
			.filter(Objects::nonNull)
			.toList();


		double averageCTR = ctrList.stream()
			.mapToDouble(Double::doubleValue)
			.average()
			.orElse(0.0);

		return new ProceedingRetrospectCTRAverageResponse(averageCTR);
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

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveRetrospectImpression(ImpressionRetrospectEvent event) {

		AdminRetrospectImpression clickEvent = AdminRetrospectImpression.builder()
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.build();

		adminRetrospectImpressionRepository.save(clickEvent);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveRetrospectClick(ClickRetrospectEvent event) {

		AdminRetrospectClick clickEvent = AdminRetrospectClick.builder()
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.spaceId(event.spaceId())
			.retrospectId(event.retrospectId())
			.retrospectStatus(AdminRetrospectStatus.from(event.retrospectStatus()))
			.build();

		adminRetrospectClickRepository.save(clickEvent);
	}
}
