package org.layer.domain.member.controller.dto;

import java.time.LocalDateTime;

import org.layer.domain.retrospect.dto.SpaceRetrospectDto;

public record GetMemberRecentGoodAnalyzeResponse(
	Long spaceId,
	String spaceName,
	Long retrospectId,
	String retrospectTitle,
	LocalDateTime deadline,
	String goodPoint
) {
	public static GetMemberRecentGoodAnalyzeResponse of(SpaceRetrospectDto spaceRetrospectDto, String goodPoint) {
		return new GetMemberRecentGoodAnalyzeResponse(spaceRetrospectDto.getSpace().getId(),
			spaceRetrospectDto.getSpace().getName(), spaceRetrospectDto.getRetrospect().getId(),
			spaceRetrospectDto.getRetrospect().getTitle(), spaceRetrospectDto.getRetrospect().getDeadline(), goodPoint);
	}

}
