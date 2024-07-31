package org.layer.domain.retrospect.controller.dto.request;

import org.layer.domain.question.enums.QuestionType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuestionCreateRequest", description = "질문 객체 생성 요청 Dto")
public record QuestionCreateRequest(
	@Schema(description = "질문 내용", example = "팀원 간의 소통은 어땠나요?")
	String questionContent,
	@Schema(description = "질문 타입", example = "plain_text")
	QuestionType questionType
) {
}
