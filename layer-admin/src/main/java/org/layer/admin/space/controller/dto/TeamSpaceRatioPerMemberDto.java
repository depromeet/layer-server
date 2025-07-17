package org.layer.admin.space.controller.dto;

public record TeamSpaceRatioPerMemberDto(
	Long memberId,
	long totalCount,
	long teamCount,
	double teamRatio
) {
	public TeamSpaceRatioPerMemberDto(Long memberId, long totalCount, long teamCount, double teamRatio) {
		this.memberId = memberId;
		this.totalCount = totalCount;
		this.teamCount = teamCount;
		this.teamRatio = teamRatio;
	}
}
