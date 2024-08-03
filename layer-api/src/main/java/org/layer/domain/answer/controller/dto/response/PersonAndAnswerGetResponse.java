package org.layer.domain.answer.controller.dto.response;

public record PersonAndAnswerGetResponse(
	String name,
	String answerContent
) {
}
