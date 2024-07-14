package org.layer.domain.question.controller.dto.response;


public record QuestionGetResponse(
	String question,
	int order
) {
	public static QuestionGetResponse of(String question, int order) {
		return new QuestionGetResponse(question, order);
	}
}
