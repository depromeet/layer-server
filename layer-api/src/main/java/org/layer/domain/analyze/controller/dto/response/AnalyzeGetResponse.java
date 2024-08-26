package org.layer.domain.analyze.controller.dto.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.layer.domain.analyze.entity.Analyze;
import org.layer.domain.analyze.enums.AnalyzeDetailType;

public record AnalyzeGetResponse(
	int satisfactionCount,
	int normalCount,
	int regretCount,
	int goalCompletionRate,
	List<AnalyzeDetailResponse> goodPoints,
	List<AnalyzeDetailResponse> badPoints,
	List<AnalyzeDetailResponse> improvementPoints

) {
	public static AnalyzeGetResponse of(Analyze analyze) {

		Map<AnalyzeDetailType, List<AnalyzeDetailResponse>> map = analyze.getAnalyzeDetails().stream()
			.map(analyzeDetail -> AnalyzeDetailResponse.of(analyzeDetail.getContent(), analyzeDetail.getCount(),
				analyzeDetail.getAnalyzeDetailType()))
			.collect(Collectors.groupingBy(AnalyzeDetailResponse::analyzeDetailType));

		return new AnalyzeGetResponse(analyze.getSatisfactionCount(), analyze.getNormalCount(),
			analyze.getRegretCount(), analyze.getGoalCompletionRate(),
			map.get(AnalyzeDetailType.GOOD), map.get(AnalyzeDetailType.BAD), map.get(AnalyzeDetailType.IMPROVEMENT));
	}
}
