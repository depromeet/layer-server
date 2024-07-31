package org.layer.domain.question.controller.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record QuestionListGetResponse(
	@Schema(description = "질문 객체 목록", example = "")
	List<QuestionGetResponse> questions,
	@Schema(description = "임시 저장 여부", example = "true")
	boolean isTemporarySaved
) {
	public static QuestionListGetResponse of(List<QuestionGetResponse> questions, boolean isTemporarySaved){
		return new QuestionListGetResponse(questions, isTemporarySaved);
	}
}
