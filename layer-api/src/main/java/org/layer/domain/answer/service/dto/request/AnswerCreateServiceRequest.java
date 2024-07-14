package org.layer.domain.answer.service.dto.request;

public record AnswerCreateServiceRequest(
	Long questionId,
	String questionType,
	String answer
) {
	public static AnswerCreateServiceRequest of(Long questionId, String questionType, String answer){
		return new AnswerCreateServiceRequest(questionId, questionType, answer);
	}
}
