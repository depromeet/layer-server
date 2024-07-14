package org.layer.domain.question.controller.dto.response;

import java.util.List;

public record QuestionListGetResponse(
	List<QuestionGetResponse> questions
) {
	public static QuestionListGetResponse of(List<QuestionGetResponse> questions){
		return new QuestionListGetResponse(questions);
	}
}
