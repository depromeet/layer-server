package org.layer.admin.retrospect.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.admin.retrospect.controller.dto.CumulativeRetrospectCountResponse;
import org.layer.admin.retrospect.controller.dto.CycleDistributionEntry;
import org.layer.admin.retrospect.controller.dto.MeaningfulRetrospectMemberResponse;
import org.layer.admin.retrospect.controller.dto.ProceedingRetrospectCTRAverageResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectCompletionRateResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectCreationCycleResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectOverviewResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectRetentionResponse;
import org.layer.admin.retrospect.controller.dto.RetrospectStayTimeResponse;
import org.layer.admin.retrospect.entity.AdminRetrospect;
import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.layer.admin.retrospect.entity.AdminRetrospectHistory;
import org.layer.admin.retrospect.entity.AdminRetrospectClick;
import org.layer.admin.retrospect.entity.AdminRetrospectImpression;
import org.layer.admin.retrospect.controller.dto.CompletionTrendResponse;
import org.layer.admin.retrospect.controller.dto.MonthlyCompletionRate;
import org.layer.admin.retrospect.controller.dto.MonthlyWritingCycle;
import org.layer.admin.retrospect.controller.dto.RetrospectFunnelResponse;
import org.layer.admin.retrospect.controller.dto.WritingCycleDistributionResponse;
import org.layer.admin.retrospect.controller.dto.WritingCycleEntry;
import org.layer.admin.retrospect.controller.dto.WritingCycleMonthlyTrendResponse;
import org.layer.admin.retrospect.enums.AdminRetrospectStatus;
import org.layer.admin.retrospect.enums.AnswerTimeRange;
import org.layer.admin.retrospect.enums.RetrospectCycleRange;
import org.layer.admin.retrospect.enums.WritingCycleRange;
import org.layer.admin.retrospect.repository.AdminRetrospectAnswerRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectClickRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectImpressionRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectHistoryRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectRepository;
import org.layer.admin.retrospect.repository.dto.ProceedingRetrospectClickDto;
import org.layer.admin.retrospect.repository.dto.ProceedingRetrospectImpressionDto;
import org.layer.admin.retrospect.repository.dto.RetrospectAnswerCompletionDto;
import org.layer.admin.retrospect.repository.dto.SpaceRetrospectCountDto;
import org.layer.admin.space.entity.AdminMemberSpaceRelation;
import org.layer.admin.space.entity.AdminSpaceHistory;
import org.layer.admin.space.enums.AdminSpaceCategory;
import org.layer.admin.space.repository.AdminMemberSpaceRelationRepository;
import org.layer.admin.space.repository.AdminSpaceRepository;
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

	private final AdminRetrospectHistoryRepository adminRetrospectHistoryRepository;
	private final AdminRetrospectAnswerRepository adminRetrospectAnswerRepository;
	private final AdminRetrospectImpressionRepository adminRetrospectImpressionRepository;
	private final AdminRetrospectClickRepository adminRetrospectClickRepository;
	private final AdminMemberRepository adminMemberRepository;
	private final AdminSpaceRepository adminSpaceRepository;
	private final AdminRetrospectRepository retrospectRepository;
	private final AdminMemberSpaceRelationRepository memberSpaceRelationRepository;

	public RetrospectOverviewResponse getRetrospectOverview(LocalDateTime startDate, LocalDateTime endDate) {
		long createdRetrospectCount = retrospectRepository.countAllByCreatedAtBetween(startDate, endDate);
		long completedRetrospectCount = retrospectRepository.countCompletedRetrospectBetween(startDate, endDate);

		double averageCompletionRate = createdRetrospectCount == 0
			? 0.0
			: (completedRetrospectCount / (double)createdRetrospectCount) * 100.0;

		double averageRetrospectLength = adminRetrospectAnswerRepository.findAverageRetrospectLengthBetween(
			startDate, endDate);
		List<AdminRetrospectAnswerHistory> completedAnswerHistories =
			adminRetrospectAnswerRepository.findAllByAnswerEndTimeBetweenAndAnswerStartTimeIsNotNullAndAnswerEndTimeIsNotNull(
				startDate, endDate);
		double averageWritingTimeMinutes = completedAnswerHistories.stream()
			.mapToLong(AdminRetrospectAnswerHistory::getAnswerTime)
			.average()
			.orElse(0.0);

		return new RetrospectOverviewResponse(
			createdRetrospectCount,
			completedRetrospectCount,
			averageCompletionRate,
			averageRetrospectLength,
			averageWritingTimeMinutes
		);
	}

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
		List<AdminRetrospectHistory> histories = adminRetrospectHistoryRepository.findAllByEventTimeBetween(
			startTime, endTime);
		List<AdminRetrospectHistory> prevHistories = adminRetrospectHistoryRepository.findAllByEventTimeBefore(startTime);

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

		List<SpaceRetrospectCountDto> histories = adminRetrospectHistoryRepository.findRetrospectCountGroupedBySpaceWithPeriod(startTime,
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

		if (answerHistories.isEmpty()) {
			return new RetrospectCompletionRateResponse(0.0);
		}

		// 필요한 회고/스페이스/팀 정보를 미리 한 번에 로딩해서 N+1 방지
		List<Long> retrospectIds = answerHistories.stream()
			.map(RetrospectAnswerCompletionDto::retrospectId)
			.distinct()
			.toList();

		List<AdminRetrospect> retrospects = retrospectRepository.findAllById(retrospectIds);
		Map<Long, AdminRetrospect> retrospectMap = retrospects.stream()
			.collect(Collectors.toMap(AdminRetrospect::getId, r -> r));

		List<Long> spaceIds = retrospects.stream()
			.map(AdminRetrospect::getSpaceId)
			.distinct()
			.toList();

		List<AdminMemberSpaceRelation> allRelations = memberSpaceRelationRepository.findAllBySpaceIdIn(spaceIds);
		Map<Long, List<AdminMemberSpaceRelation>> relationsBySpaceId =
			allRelations.stream()
				.collect(Collectors.groupingBy(
					AdminMemberSpaceRelation::getSpaceId
				));

		// 회고별 분모를 도메인 로직(Team, RetrospectStatus, deadline) 기반으로 계산
		List<Double> completionRates = answerHistories.stream()
			.map(dto -> {
				AdminRetrospect retrospect = retrospectMap.get(dto.retrospectId());
				if (retrospect == null) {
					return null;
				}

				List<AdminMemberSpaceRelation> relationList = relationsBySpaceId.get(retrospect.getSpaceId());
				if (relationList == null) {
					return null;
				}

				long totalCount = relationList.size();
				if (retrospect.getRetrospectStatus().equals(AdminRetrospectStatus.DONE)) {
					// 회고가 종료된 경우, deadline 시점의 팀원 수를 분모로 사용
					totalCount = getTeamMemberCountBefore(relationList, retrospect.getDeadline());
				}

				if (totalCount == 0) {
					return null; // division by zero 방지
				}

				return (double) dto.actualAnswerCount() / totalCount * 100.0;
			})
			.filter(Objects::nonNull)
			.toList();

		double averageCompletionRate = completionRates.isEmpty()
			? 0.0
			: completionRates.stream()
			.mapToDouble(Double::doubleValue)
			.average()
			.orElse(0.0);

		return new RetrospectCompletionRateResponse(averageCompletionRate);
	}

	private long getTeamMemberCountBefore(List<AdminMemberSpaceRelation> relationList, LocalDateTime end) {
		return relationList.stream()
			.filter(memberSpaceRelation -> memberSpaceRelation.getCreatedAt().isBefore(end))
			.count();
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

	public RetrospectCreationCycleResponse getRetrospectCreationCycle(LocalDateTime startDate, LocalDateTime endDate) {
		List<AdminRetrospectHistory> currentHistories = adminRetrospectHistoryRepository.findAllByEventTimeBetween(startDate, endDate);

		if (currentHistories.isEmpty()) {
			return new RetrospectCreationCycleResponse(0.0, 0.0, 0.0, buildEmptyCycleDistribution());
		}

		List<AdminRetrospectHistory> prevHistories = adminRetrospectHistoryRepository.findAllByEventTimeBefore(startDate);

		Set<Long> allSpaceIds = new HashSet<>();
		currentHistories.forEach(h -> allSpaceIds.add(h.getSpaceId()));
		prevHistories.forEach(h -> allSpaceIds.add(h.getSpaceId()));

		Map<Long, AdminSpaceCategory> spaceCategoryMap = adminSpaceRepository.findAllBySpaceIdIn(allSpaceIds)
			.stream()
			.collect(Collectors.toMap(AdminSpaceHistory::getSpaceId, AdminSpaceHistory::getCategory, (a, b) -> a));

		Map<Long, List<AdminRetrospectHistory>> allHistoriesByMember = new HashMap<>();
		prevHistories.forEach(h -> allHistoriesByMember.computeIfAbsent(h.getMemberId(), k -> new ArrayList<>()).add(h));
		currentHistories.forEach(h -> allHistoriesByMember.computeIfAbsent(h.getMemberId(), k -> new ArrayList<>()).add(h));

		Set<Long> currentMemberIds = currentHistories.stream()
			.map(AdminRetrospectHistory::getMemberId)
			.collect(Collectors.toSet());

		List<Long> allGaps = new ArrayList<>();
		List<Long> teamGaps = new ArrayList<>();
		List<Long> individualGaps = new ArrayList<>();
		Map<Long, Double> memberAvgGapMap = new HashMap<>();

		for (Long memberId : currentMemberIds) {
			List<AdminRetrospectHistory> memberHistories = allHistoriesByMember.getOrDefault(memberId, Collections.emptyList());
			memberHistories.sort(Comparator.comparing(AdminRetrospectHistory::getEventTime));

			List<Long> memberAllGaps = new ArrayList<>();
			for (int i = 1; i < memberHistories.size(); i++) {
				AdminRetrospectHistory curr = memberHistories.get(i);
				if (curr.getEventTime().isBefore(startDate)) {
					continue;
				}
				long gapDays = Duration.between(memberHistories.get(i - 1).getEventTime(), curr.getEventTime()).toDays();
				if (gapDays <= 0) {
					continue;
				}
				memberAllGaps.add(gapDays);
				allGaps.add(gapDays);
			}

			if (!memberAllGaps.isEmpty()) {
				memberAvgGapMap.put(memberId, memberAllGaps.stream().mapToLong(Long::longValue).average().orElse(0.0));
			}

			List<AdminRetrospectHistory> teamRetros = memberHistories.stream()
				.filter(h -> AdminSpaceCategory.TEAM == spaceCategoryMap.get(h.getSpaceId()))
				.sorted(Comparator.comparing(AdminRetrospectHistory::getEventTime))
				.toList();

			for (int i = 1; i < teamRetros.size(); i++) {
				AdminRetrospectHistory curr = teamRetros.get(i);
				if (curr.getEventTime().isBefore(startDate)) {
					continue;
				}
				long gapDays = Duration.between(teamRetros.get(i - 1).getEventTime(), curr.getEventTime()).toDays();
				if (gapDays > 0) {
					teamGaps.add(gapDays);
				}
			}

			List<AdminRetrospectHistory> individualRetros = memberHistories.stream()
				.filter(h -> AdminSpaceCategory.INDIVIDUAL == spaceCategoryMap.get(h.getSpaceId()))
				.sorted(Comparator.comparing(AdminRetrospectHistory::getEventTime))
				.toList();

			for (int i = 1; i < individualRetros.size(); i++) {
				AdminRetrospectHistory curr = individualRetros.get(i);
				if (curr.getEventTime().isBefore(startDate)) {
					continue;
				}
				long gapDays = Duration.between(individualRetros.get(i - 1).getEventTime(), curr.getEventTime()).toDays();
				if (gapDays > 0) {
					individualGaps.add(gapDays);
				}
			}
		}

		double overallAvg = allGaps.stream().mapToLong(Long::longValue).average().orElse(0.0);
		double teamAvg = teamGaps.stream().mapToLong(Long::longValue).average().orElse(0.0);
		double individualAvg = individualGaps.stream().mapToLong(Long::longValue).average().orElse(0.0);

		return new RetrospectCreationCycleResponse(overallAvg, teamAvg, individualAvg, buildCycleDistribution(memberAvgGapMap));
	}

	private List<CycleDistributionEntry> buildCycleDistribution(Map<Long, Double> memberAvgGapMap) {
		if (memberAvgGapMap.isEmpty()) {
			return buildEmptyCycleDistribution();
		}

		Map<RetrospectCycleRange, Long> bucketCounts = new LinkedHashMap<>();
		for (RetrospectCycleRange range : RetrospectCycleRange.values()) {
			bucketCounts.put(range, 0L);
		}

		memberAvgGapMap.values().forEach(avgGap ->
			bucketCounts.merge(RetrospectCycleRange.from(avgGap), 1L, Long::sum)
		);

		double total = memberAvgGapMap.size();
		return Arrays.stream(RetrospectCycleRange.values())
			.map(range -> new CycleDistributionEntry(
				range.getLabel(),
				Math.round(bucketCounts.get(range) / total * 1000.0) / 10.0
			))
			.collect(Collectors.toList());
	}

	private List<CycleDistributionEntry> buildEmptyCycleDistribution() {
		return Arrays.stream(RetrospectCycleRange.values())
			.map(range -> new CycleDistributionEntry(range.getLabel(), 0.0))
			.collect(Collectors.toList());
	}

	public WritingCycleDistributionResponse getWritingCycleDistribution(LocalDateTime startDate, LocalDateTime endDate) {
		List<AdminRetrospectAnswerHistory> currentAnswers =
			adminRetrospectAnswerRepository.findAllByAnswerEndTimeBetween(startDate, endDate);

		if (currentAnswers.isEmpty()) {
			return new WritingCycleDistributionResponse(0.0, buildEmptyWritingCycleDistribution());
		}

		List<AdminRetrospectAnswerHistory> prevAnswers =
			adminRetrospectAnswerRepository.findAllByAnswerEndTimeBefore(startDate);

		Map<Long, Double> memberAvgGapMap = computeMemberWritingAvgGaps(currentAnswers, prevAnswers, startDate);
		double overall = memberAvgGapMap.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

		return new WritingCycleDistributionResponse(overall, buildWritingCycleDistribution(memberAvgGapMap));
	}

	public WritingCycleMonthlyTrendResponse getWritingCycleMonthlyTrend(LocalDateTime endDate) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
		YearMonth endMonth = YearMonth.from(endDate);
		List<MonthlyWritingCycle> months = new ArrayList<>();

		for (int i = 5; i >= 0; i--) {
			YearMonth ym = endMonth.minusMonths(i);
			LocalDateTime from = ym.atDay(1).atStartOfDay();
			LocalDateTime to = ym.atEndOfMonth().atTime(23, 59, 59);

			List<AdminRetrospectAnswerHistory> current =
				adminRetrospectAnswerRepository.findAllByAnswerEndTimeBetween(from, to);
			List<AdminRetrospectAnswerHistory> prev =
				adminRetrospectAnswerRepository.findAllByAnswerEndTimeBefore(from);

			Map<Long, Double> memberMap = computeMemberWritingAvgGaps(current, prev, from);
			months.add(new MonthlyWritingCycle(ym.format(fmt), buildWritingCycleDistribution(memberMap)));
		}

		return new WritingCycleMonthlyTrendResponse(months);
	}

	public RetrospectFunnelResponse getRetrospectFunnel(LocalDateTime startDate, LocalDateTime endDate) {
		long created = retrospectRepository.countAllByCreatedAtBetween(startDate, endDate);
		long started = adminRetrospectAnswerRepository.countDistinctRetrospectIdByAnswerStartTimeBetween(startDate, endDate);
		long quality = adminRetrospectAnswerRepository.countDistinctRetrospectIdByAnswerEndTimeAndQuality(startDate, endDate, 10);
		long submitted = adminRetrospectAnswerRepository.countDistinctRetrospectIdByAnswerEndTimeBetween(startDate, endDate);

		double startedRate = created == 0 ? 0.0 : started * 100.0 / created;
		double qualityRate = created == 0 ? 0.0 : quality * 100.0 / created;
		double submittedRate = created == 0 ? 0.0 : submitted * 100.0 / created;

		return new RetrospectFunnelResponse(created, started, startedRate, quality, qualityRate, submitted, submittedRate);
	}

	public CompletionTrendResponse getCompletionTrend(LocalDateTime endDate) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
		YearMonth endMonth = YearMonth.from(endDate);
		List<MonthlyCompletionRate> months = new ArrayList<>();

		for (int i = 11; i >= 0; i--) {
			YearMonth ym = endMonth.minusMonths(i);
			LocalDateTime from = ym.atDay(1).atStartOfDay();
			LocalDateTime to = ym.atEndOfMonth().atTime(23, 59, 59);

			List<AdminRetrospect> retrospects = retrospectRepository.findAllByCreatedAtBetween(from, to);

			if (retrospects.isEmpty()) {
				months.add(new MonthlyCompletionRate(ym.format(fmt), 0.0, 0.0, 0.0));
				continue;
			}

			Set<Long> allSpaceIds = retrospects.stream().map(AdminRetrospect::getSpaceId).collect(Collectors.toSet());
			Map<Long, AdminSpaceCategory> categoryMap = adminSpaceRepository.findAllBySpaceIdIn(allSpaceIds)
				.stream()
				.collect(Collectors.toMap(AdminSpaceHistory::getSpaceId, AdminSpaceHistory::getCategory, (a, b) -> a));

			long totalCreated = retrospects.size();
			long totalDone = retrospects.stream().filter(r -> AdminRetrospectStatus.DONE == r.getRetrospectStatus()).count();
			double overallRate = totalCreated == 0 ? 0.0 : totalDone * 100.0 / totalCreated;

			List<AdminRetrospect> teamRetros = retrospects.stream()
				.filter(r -> AdminSpaceCategory.TEAM == categoryMap.get(r.getSpaceId()))
				.toList();
			long teamDone = teamRetros.stream().filter(r -> AdminRetrospectStatus.DONE == r.getRetrospectStatus()).count();
			double teamRate = teamRetros.isEmpty() ? 0.0 : teamDone * 100.0 / teamRetros.size();

			List<AdminRetrospect> indRetros = retrospects.stream()
				.filter(r -> AdminSpaceCategory.INDIVIDUAL == categoryMap.get(r.getSpaceId()))
				.toList();
			long indDone = indRetros.stream().filter(r -> AdminRetrospectStatus.DONE == r.getRetrospectStatus()).count();
			double indRate = indRetros.isEmpty() ? 0.0 : indDone * 100.0 / indRetros.size();

			months.add(new MonthlyCompletionRate(ym.format(fmt), overallRate, teamRate, indRate));
		}

		return new CompletionTrendResponse(months);
	}

	private Map<Long, Double> computeMemberWritingAvgGaps(
		List<AdminRetrospectAnswerHistory> current,
		List<AdminRetrospectAnswerHistory> prev,
		LocalDateTime startDate
	) {
		Map<Long, List<LocalDateTime>> timesByMember = new HashMap<>();
		prev.stream()
			.filter(a -> a.getAnswerEndTime() != null)
			.forEach(a -> timesByMember.computeIfAbsent(a.getMemberId(), k -> new ArrayList<>()).add(a.getAnswerEndTime()));
		current.stream()
			.filter(a -> a.getAnswerEndTime() != null)
			.forEach(a -> timesByMember.computeIfAbsent(a.getMemberId(), k -> new ArrayList<>()).add(a.getAnswerEndTime()));

		Set<Long> currentMemberIds = current.stream().map(AdminRetrospectAnswerHistory::getMemberId).collect(Collectors.toSet());
		Map<Long, Double> memberAvgGapMap = new HashMap<>();

		for (Long memberId : currentMemberIds) {
			List<LocalDateTime> times = timesByMember.getOrDefault(memberId, Collections.emptyList())
				.stream().sorted().toList();
			List<Long> gaps = new ArrayList<>();
			for (int i = 1; i < times.size(); i++) {
				if (times.get(i).isBefore(startDate)) continue;
				long days = Duration.between(times.get(i - 1), times.get(i)).toDays();
				if (days > 0) gaps.add(days);
			}
			if (!gaps.isEmpty()) {
				memberAvgGapMap.put(memberId, gaps.stream().mapToLong(Long::longValue).average().orElse(0.0));
			}
		}
		return memberAvgGapMap;
	}

	private List<WritingCycleEntry> buildWritingCycleDistribution(Map<Long, Double> memberAvgGapMap) {
		if (memberAvgGapMap.isEmpty()) return buildEmptyWritingCycleDistribution();

		Map<WritingCycleRange, Long> buckets = new LinkedHashMap<>();
		for (WritingCycleRange r : WritingCycleRange.values()) buckets.put(r, 0L);
		memberAvgGapMap.values().forEach(avg -> buckets.merge(WritingCycleRange.from(avg), 1L, Long::sum));

		double total = memberAvgGapMap.size();
		return Arrays.stream(WritingCycleRange.values())
			.map(r -> new WritingCycleEntry(r.getLabel(), Math.round(buckets.get(r) / total * 1000.0) / 10.0, buckets.get(r)))
			.collect(Collectors.toList());
	}

	private List<WritingCycleEntry> buildEmptyWritingCycleDistribution() {
		return Arrays.stream(WritingCycleRange.values())
			.map(r -> new WritingCycleEntry(r.getLabel(), 0.0, 0L))
			.collect(Collectors.toList());
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

		adminRetrospectHistoryRepository.save(retrospectHistory);
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
