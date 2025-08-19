package org.layer.admin.space.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.layer.admin.retrospect.enums.AdminRetrospectStatus;
import org.layer.admin.retrospect.repository.AdminRetrospectRepository;
import org.layer.admin.space.controller.dto.ProceedingSpaceCTRAverageResponse;
import org.layer.admin.space.controller.dto.SpaceAbandonRate;
import org.layer.admin.space.entity.AdminSpaceClick;
import org.layer.admin.space.entity.AdminSpaceImpression;
import org.layer.admin.space.repository.AdminMemberSpaceRelationRepository;
import org.layer.admin.space.repository.AdminSpaceImpressionRepository;
import org.layer.admin.space.repository.dto.ProceedingSpaceClickDto;
import org.layer.admin.space.controller.dto.SpaceCountResponse;
import org.layer.admin.space.controller.dto.TeamSpaceRatioPerMemberDto;
import org.layer.admin.space.controller.dto.TeamSpaceRatioResponse;
import org.layer.admin.space.entity.AdminMemberSpaceHistory;
import org.layer.admin.space.entity.AdminSpaceHistory;
import org.layer.admin.space.enums.AdminSpaceCategory;
import org.layer.admin.space.repository.AdminMemberSpaceRepository;
import org.layer.admin.space.repository.AdminSpaceClickRepository;
import org.layer.admin.space.repository.AdminSpaceRepository;
import org.layer.admin.space.repository.dto.ProceedingSpaceDto;
import org.layer.admin.space.repository.dto.ProceedingSpaceImpressionDto;
import org.layer.event.space.ClickSpaceEvent;
import org.layer.event.space.CreateSpaceEvent;
import org.layer.event.space.ImpressionSpaceEvent;
import org.layer.event.space.JoinSpaceEvent;
import org.layer.event.space.LeaveSpaceEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSpaceService {

	private final AdminSpaceRepository adminSpaceRepository;
	private final AdminRetrospectRepository adminRetrospectRepository;
	private final AdminMemberSpaceRepository adminMemberSpaceRepository;
	private final AdminSpaceImpressionRepository adminSpaceImpressionRepository;
	private final AdminSpaceClickRepository adminSpaceClickRepository;
	private final AdminMemberSpaceRelationRepository adminMemberSpaceRelationRepository;

	public List<SpaceCountResponse> getSpaceCount(LocalDateTime startDate, LocalDateTime endDate) {
		return adminSpaceRepository.findAllByCategory(startDate, endDate);
	}

	public TeamSpaceRatioResponse getAverageTeamSpaceRatioPerMember(
		LocalDateTime startDate, LocalDateTime endDate, int page, int size) {

		Page<TeamSpaceRatioPerMemberDto> histories = adminMemberSpaceRepository.findTeamSpaceRatioPerMemberWithPeriod(
			startDate, endDate, PageRequest.of(page - 1, size));
		if (histories.isEmpty()) {
			return new TeamSpaceRatioResponse(null, 0.0, false, 0);
		}

		double averageTeamSpaceRatioPerMember = adminMemberSpaceRepository.findAverageOfTeamSpaceRatiosWithPeriod(startDate,
			endDate);

		return TeamSpaceRatioResponse.of(histories.getContent(), averageTeamSpaceRatioPerMember, histories.hasNext(),
			histories.getTotalPages());
	}

	public ProceedingSpaceCTRAverageResponse getProceedingSpaceCTR(LocalDateTime startDate, LocalDateTime endDate){
		List<ProceedingSpaceImpressionDto> impressions = adminSpaceImpressionRepository.findProceedingSpaceImpressionGroupByMember(
			startDate, endDate);
		List<ProceedingSpaceClickDto> clicks = adminSpaceClickRepository.findProceedingSpaceClickGroupByMember(
			startDate, endDate);

		Map<Long, Long> impressionMap = impressions.stream()
			.collect(Collectors.toMap(
				ProceedingSpaceImpressionDto::memberId,
				ProceedingSpaceImpressionDto::totalCount
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

		return new ProceedingSpaceCTRAverageResponse(averageCTR);
	}

	public SpaceAbandonRate calculateAbandonRate(LocalDateTime startDate, LocalDateTime endDate, long day) {
		LocalDateTime threshold = endDate.minusDays(day);

		List<Long> proceedingSpaceIds = adminRetrospectRepository.findProceedingSpacesByMember(startDate, endDate);

		Map<Long, Long> spaceMemberCountMap = adminMemberSpaceRelationRepository.findProceedingSpacesWithMemberCount(startDate,
				endDate)
			.stream()
			.collect(Collectors.toMap(
				ProceedingSpaceDto::spaceId,
				ProceedingSpaceDto::memberCount
			));

		List<AdminSpaceClick> clicks = adminSpaceClickRepository.findAllByEventTimeBetween(threshold, endDate);
		Map<Long, Long> clickCountMap = clicks.stream()
			.collect(Collectors.groupingBy(
				AdminSpaceClick::getSpaceId, // spaceId로 그룹화
				Collectors.collectingAndThen(
					Collectors.mapping(
						AdminSpaceClick::getMemberId, // memberId만 추출
						Collectors.toSet() // 중복 제거
					),
					set -> (long) set.size() // Set 크기를 Long으로 변환
				)
			));

		double abandonRate = proceedingSpaceIds.stream()
			.map(spaceId -> {
				Long memberCount = spaceMemberCountMap.getOrDefault(spaceId, 0L);
				Long clickCount = clickCountMap.getOrDefault(spaceId, 0L);

				if (memberCount == 0) {
					return null; // 멤버가 없는 경우 제외
				}

				return (memberCount - clickCount) / (double)memberCount;
			})
			.filter(Objects::nonNull) // null 제거
			.mapToDouble(Double::doubleValue) // double 스트림으로 변환
			.average() // 평균 계산
			.orElse(0.0);// 값이 없을 경우 0.0

		return new SpaceAbandonRate(abandonRate);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveSpaceHistory(CreateSpaceEvent event) {
		AdminSpaceHistory spaceHistory = AdminSpaceHistory.builder()
			.eventId(event.eventId())
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.category(AdminSpaceCategory.from(event.category()))
			.spaceId(event.spaceId())
			.build();

		adminSpaceRepository.save(spaceHistory);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveMemberSpaceHistory(JoinSpaceEvent event) {
		AdminMemberSpaceHistory memberSpaceHistory = AdminMemberSpaceHistory.builder()
			.eventId(event.eventId())
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.spaceId(event.spaceId())
			.build();

		adminMemberSpaceRepository.save(memberSpaceHistory);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void deleteMemberSpaceHistory(LeaveSpaceEvent event) {
		adminMemberSpaceRepository.deleteByMemberIdAndSpaceId(
			event.memberId(), event.spaceId());
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveSpaceImpression(ImpressionSpaceEvent event) {
		AdminSpaceImpression impressionClick = AdminSpaceImpression.builder()
			.eventId(event.eventId())
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.build();

		adminSpaceImpressionRepository.save(impressionClick);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveSpaceClick(ClickSpaceEvent event) {
		AdminSpaceClick impressionClick = AdminSpaceClick.builder()
			.eventId(event.eventId())
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.spaceId(event.spaceId())
			.retrospectStatus(AdminRetrospectStatus.from(event.retrospectStatus()))
			.build();

		adminSpaceClickRepository.save(impressionClick);
	}
}
