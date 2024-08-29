package org.layer.domain.member.controller.dto;

import java.time.LocalDateTime;

import org.layer.domain.retrospect.dto.SpaceRetrospectDto;

public record GetMemberRecentImprovementAnalyzeResponse(
	Long spaceId,
	String spaceName,
	Long retrospectId,
	String retrospectTitle,
	LocalDateTime deadline,
	String improvementPoint
) {
	public static GetMemberRecentImprovementAnalyzeResponse of(SpaceRetrospectDto spaceRetrospectDto, String improvementPoint) {
		return new GetMemberRecentImprovementAnalyzeResponse(spaceRetrospectDto.getSpace().getId(),
			spaceRetrospectDto.getSpace().getName(), spaceRetrospectDto.getRetrospect().getId(),
			spaceRetrospectDto.getRetrospect().getTitle(), spaceRetrospectDto.getRetrospect().getDeadline(), improvementPoint);
	}
}
