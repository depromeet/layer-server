package org.layer.domain.analyze.controller.dto.response;

import org.layer.domain.analyze.enums.AnalyzeDetailType;

public record AnalyzeDetailResponse(
	String content,
	int count,
	AnalyzeDetailType analyzeDetailType
) {
	public static AnalyzeDetailResponse of(String content, int count, AnalyzeDetailType analyzeDetailType){
		return new AnalyzeDetailResponse(content, count, analyzeDetailType);
	}
}
