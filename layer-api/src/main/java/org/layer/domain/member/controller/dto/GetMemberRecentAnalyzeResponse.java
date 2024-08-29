package org.layer.domain.member.controller.dto;

import java.time.LocalDateTime;

import org.layer.domain.retrospect.dto.SpaceRetrospectDto;

public record GetMemberRecentAnalyzeResponse(
	Long spaceId,
	String spaceName,
	Long retrospectId,
	String retrospectTitle,
	LocalDateTime deadline
) {
	public static GetMemberRecentAnalyzeResponse of(SpaceRetrospectDto spaceRetrospectDto) {
		return new GetMemberRecentAnalyzeResponse(spaceRetrospectDto.getSpace().getId(),
			spaceRetrospectDto.getSpace().getName(), spaceRetrospectDto.getRetrospect().getId(),
			spaceRetrospectDto.getRetrospect().getTitle(), spaceRetrospectDto.getRetrospect().getDeadline());
	}

}
