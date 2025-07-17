package org.layer.admin.space.controller.dto;

import java.util.List;

public record TeamSpaceRatioResponse(
	List<TeamSpaceRatioPerMemberDto> averageTeamSpaceRatios,
	double averageTeamSpaceRatioPerMember,
	boolean hasNext,
	long totalPages
) {
	public static TeamSpaceRatioResponse of(
		List<TeamSpaceRatioPerMemberDto> averageTeamSpaceRatios,
		double averageTeamSpaceRatioPerMember,
		boolean hasNext,
		long totalPages
	) {
		return new TeamSpaceRatioResponse(
			averageTeamSpaceRatios,
			averageTeamSpaceRatioPerMember,
			hasNext,
			totalPages
		);
	}

}
