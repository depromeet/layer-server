package org.layer.admin.space.controller.dto;

public record TeamSpaceRatioPerMemberDto(
	Long memberId,
	long totalCount,
	long teamCount
) {
	public TeamSpaceRatioPerMemberDto(Long memberId, long totalCount, long teamCount) {
		this.memberId = memberId;
		this.totalCount = totalCount;
		this.teamCount = teamCount;
	}
}
