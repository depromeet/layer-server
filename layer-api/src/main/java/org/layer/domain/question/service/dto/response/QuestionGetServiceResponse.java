package org.layer.domain.question.service.dto.response;

public record QuestionGetServiceResponse(
	Long questionId,
	String question,
	int order,
	String questionType
) {
	public static QuestionGetServiceResponse of(Long questionId, String question, int order, String questionType) {
		return new QuestionGetServiceResponse(questionId, question, order, questionType);
	}
}
