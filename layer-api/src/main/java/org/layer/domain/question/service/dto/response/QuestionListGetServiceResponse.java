package org.layer.domain.question.service.dto.response;

import java.util.List;

public record QuestionListGetServiceResponse(
	List<QuestionGetServiceResponse> questions
) {
	public static QuestionListGetServiceResponse of(List<QuestionGetServiceResponse> questions){
		return new QuestionListGetServiceResponse(questions);
	}
}
