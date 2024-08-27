package org.layer.domain.analyze.controller.dto.response;

public record AnalyzesGetResponse(
	AnalyzeTeamGetResponse teamAnalyze,
	AnalyzeIndividualGetResponse individualAnalyze

	) {
	public static AnalyzesGetResponse of(AnalyzeTeamGetResponse teamAnalyze, AnalyzeIndividualGetResponse individualAnalyze){
		return new AnalyzesGetResponse(teamAnalyze, individualAnalyze);
	}
}
