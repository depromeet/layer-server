package org.layer.domain.answer.controller.dto.response;

import java.util.List;

public record AnswerByPersonGetResponse(
	String name,
	List<QuestionAndAnswerGetResponse> answers
) {
}
