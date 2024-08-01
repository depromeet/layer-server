package org.layer.domain.form.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DefaultQuestionGetResponse", description = "기본 회고폼(커스텀 템플릿) 질문 조회 응답 Dto")
public record DefaultQuestionGetResponse(
	@Schema(description = "질문 내용", example = "계속 유지하고 싶은 것은 무엇인가요?")
	String questionContent,
	@Schema(description = "질문 타입", example = "plain_text")
	String questionType
) {
	public static DefaultQuestionGetResponse of(String questionContent, String questionType){
		return new DefaultQuestionGetResponse(questionContent, questionType);
	}
}
