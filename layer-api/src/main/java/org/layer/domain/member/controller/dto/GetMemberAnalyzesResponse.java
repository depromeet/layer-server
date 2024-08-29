package org.layer.domain.member.controller.dto;

import java.util.List;

public record GetMemberAnalyzesResponse(
	List<GetMemberRecentAnalyzeResponse> recentAnalyzes,
	List<GetMemberRecentGoodAnalyzeResponse> goodAnalyzes,
	List<GetMemberRecentBadAnalyzeResponse> badAnalyzes,
	List<GetMemberRecentImprovementAnalyzeResponse> improvementAnalyzes

) {

	public static GetMemberAnalyzesResponse of(List<GetMemberRecentAnalyzeResponse> recentAnalyzes,
		List<GetMemberRecentGoodAnalyzeResponse> goodAnalyzes,
		List<GetMemberRecentBadAnalyzeResponse> badAnalyzes,
		List<GetMemberRecentImprovementAnalyzeResponse> improvementAnalyzes) {

		return new GetMemberAnalyzesResponse(recentAnalyzes, goodAnalyzes, badAnalyzes, improvementAnalyzes);
	}
}
