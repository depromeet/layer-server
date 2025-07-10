package org.layer.admin.space.service;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.space.controller.dto.SpaceCountResponse;
import org.layer.admin.space.entity.AdminSpaceHistory;
import org.layer.admin.space.enums.AdminSpaceCategory;
import org.layer.admin.space.repository.AdminSpaceRepository;
import org.layer.event.space.CreateSpaceEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSpaceService {

	private final AdminSpaceRepository adminSpaceRepository;

	public List<SpaceCountResponse> getSpaceCount(LocalDateTime startDate, LocalDateTime endDate) {
		return adminSpaceRepository.findAllByCategory(startDate, endDate);
	}

	@Transactional
	public void saveSpaceHistory(CreateSpaceEvent event) {
		AdminSpaceHistory adminMemberSignupHistory = AdminSpaceHistory.builder()
			.eventId(event.eventId())
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.category(AdminSpaceCategory.from(event.category()))
			.build();

		adminSpaceRepository.save(adminMemberSignupHistory);
	}
}
