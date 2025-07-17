package org.layer.admin.space.repository;

import java.time.LocalDateTime;

import org.layer.admin.space.controller.dto.TeamSpaceRatioPerMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSpaceRepositoryCustom {
	Page<TeamSpaceRatioPerMemberDto> findTeamSpaceRatioPerMemberWithPeriod(LocalDateTime startTime,
		LocalDateTime endTime, Pageable pageable);

	double findAverageOfTeamSpaceRatiosWithPeriod(LocalDateTime startTime, LocalDateTime endTime);

}
