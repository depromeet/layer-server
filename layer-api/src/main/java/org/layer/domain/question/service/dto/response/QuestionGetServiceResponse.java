package org.layer.domain.question.service.dto.response;

public record QuestionGetServiceResponse(
	String question,
	int order
) {
	public static QuestionGetServiceResponse of(String question, int order) {
		return new QuestionGetServiceResponse(question, order);
	}
}
