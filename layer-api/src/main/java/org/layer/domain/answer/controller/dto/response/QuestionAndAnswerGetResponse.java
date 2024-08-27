package org.layer.domain.answer.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuestionAndAnswerGetResponse", description = "질문-답변 Dto")
public record QuestionAndAnswerGetResponse(
	@Schema(description = "질문 내용", example = "질문 내용입니다.")
	String questionContent,
	@Schema(description = "질문 타입", example = "plain_text")
	String questionType,
	@Schema(description = "답변 내용", example = "답변 내용입니다.")
	String answerContent
) {
}
