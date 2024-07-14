package org.layer.domain.question.controller.dto.response;


public record QuestionGetResponse(
	String question,
	int order,
	String questionType
) {
	public static QuestionGetResponse of(String question, int order, String questionType) {
		return new QuestionGetResponse(question, order, questionType);
	}
}
