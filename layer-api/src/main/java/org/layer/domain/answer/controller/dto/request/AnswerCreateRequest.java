package org.layer.domain.answer.controller.dto.request;

public record AnswerCreateRequest(
	Long questionId,
	String questionType,
	String answer

) {
}
