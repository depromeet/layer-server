package org.layer.domain.analyze.controller.dto.response;

public record AnalyzesGetResponse(
	AnalyzeGetResponse teamAnalyze,
	AnalyzeGetResponse individualAnalyze

	) {
	public static AnalyzesGetResponse of(AnalyzeGetResponse teamAnalyze, AnalyzeGetResponse individualAnalyze){
		return new AnalyzesGetResponse(teamAnalyze, individualAnalyze);
	}
}
