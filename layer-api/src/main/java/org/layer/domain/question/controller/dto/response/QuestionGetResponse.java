package org.layer.domain.question.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuestionGetResponse", description = "회고 질문 응답 Dto")
public record QuestionGetResponse(
	@Schema(description = "질문 id", example = "1")
	Long questionId,
	@Schema(description = "질문 내용", example = "가장 좋았던 점은 무엇이었나요?")
	String question,
	int order,
	String questionType
) {
	public static QuestionGetResponse of(Long questionId, String question, int order, String questionType) {
		return new QuestionGetResponse(questionId, question, order, questionType);
	}
}
