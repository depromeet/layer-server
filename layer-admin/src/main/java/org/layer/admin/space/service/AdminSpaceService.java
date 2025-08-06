package org.layer.admin.space.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.retrospect.enums.AdminRetrospectStatus;
import org.layer.admin.space.controller.dto.SpaceCountResponse;
import org.layer.admin.space.controller.dto.TeamSpaceRatioPerMemberDto;
import org.layer.admin.space.controller.dto.TeamSpaceRatioResponse;
import org.layer.admin.space.entity.AdminMemberSpaceHistory;
import org.layer.admin.space.entity.AdminSpaceHistory;
import org.layer.admin.space.entity.AdminSpaceImpressionClick;
import org.layer.admin.space.enums.AdminSpaceCategory;
import org.layer.admin.space.repository.AdminMemberSpaceRepository;
import org.layer.admin.space.repository.AdminSpaceImpressionClickRepository;
import org.layer.admin.space.repository.AdminSpaceRepository;
import org.layer.event.space.ClickSpaceEvent;
import org.layer.event.space.CreateSpaceEvent;
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
	private final AdminMemberSpaceRepository adminMemberSpaceRepository;
	private final AdminSpaceImpressionClickRepository adminSpaceImpressionClickRepository;

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
	public void saveSpaceImpressionClick(ClickSpaceEvent event) {
		AdminSpaceImpressionClick impressionClick = AdminSpaceImpressionClick.builder()
			.eventId(event.eventId())
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.spaceId(event.spaceId())
			.retrospectStatus(AdminRetrospectStatus.from(event.retrospectStatus()))
			.build();

		adminSpaceImpressionClickRepository.save(impressionClick);
	}
}
