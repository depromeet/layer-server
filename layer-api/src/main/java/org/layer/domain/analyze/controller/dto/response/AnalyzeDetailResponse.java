package org.layer.domain.analyze.controller.dto.response;

import org.layer.domain.analyze.enums.AnalyzeType;


public record AnalyzeDetailResponse(
	String content,
	int count,
	AnalyzeType analyzeType
) {
	public static AnalyzeDetailResponse of(String content, int count, AnalyzeType analyzeType){
		return new AnalyzeDetailResponse(content, count, analyzeType);
	}
}
