package org.layer.domain.answer.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PersonAndAnswerGetResponse", description = "개인-답변 응답 Dto")
public record PersonAndAnswerGetResponse(
	@Schema(description = "답변자", example = "홍길동")
	String name,
	@Schema(description = "답변 내용", example = "답변 내용입니당")
	String answerContent
) {
}
