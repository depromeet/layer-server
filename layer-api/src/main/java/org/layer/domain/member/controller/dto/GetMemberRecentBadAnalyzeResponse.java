package org.layer.domain.member.controller.dto;

import java.time.LocalDateTime;

import org.layer.domain.retrospect.dto.SpaceRetrospectDto;

public record GetMemberRecentBadAnalyzeResponse(
	Long spaceId,
	String spaceName,
	Long retrospectId,
	String retrospectTitle,
	LocalDateTime deadline,
	String badPoint
) {
	public static GetMemberRecentBadAnalyzeResponse of(SpaceRetrospectDto spaceRetrospectDto, String badPoint) {
		return new GetMemberRecentBadAnalyzeResponse(spaceRetrospectDto.getSpace().getId(),
			spaceRetrospectDto.getSpace().getName(), spaceRetrospectDto.getRetrospect().getId(),
			spaceRetrospectDto.getRetrospect().getTitle(), spaceRetrospectDto.getRetrospect().getDeadline(), badPoint);
	}
}
