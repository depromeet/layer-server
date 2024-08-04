package org.layer.domain.answer.controller.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AnswerListGetResponse", description = "회고 결과 조회 응답 Dto")
public record AnswerListGetResponse(
	@Schema(description = "질문 기준 결과", example = "")
	List<AnswerByQuestionGetResponse> questions,
	@Schema(description = "개별 기준 결과", example = "")
	List<AnswerByPersonGetResponse> individuals
) {
}
