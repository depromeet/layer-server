package org.layer.domain.question.service.dto.response;

public record QuestionGetServiceResponse(
	String question,
	int order,
	String questionType
) {
	public static QuestionGetServiceResponse of(String question, int order, String questionType) {
		return new QuestionGetServiceResponse(question, order, questionType);
	}
}
