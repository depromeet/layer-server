package org.layer.domain.answer.controller.dto.response;

public record QuestionAndAnswerGetResponse(
	String questionContent,
	String questionType,
	String answerContent
) {
}
