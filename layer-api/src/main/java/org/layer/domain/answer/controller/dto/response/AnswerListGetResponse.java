package org.layer.domain.answer.controller.dto.response;

import java.util.List;

public record AnswerListGetResponse(
	List<AnswerByQuestionGetResponse> questions,
	List<AnswerByPersonGetResponse> individuals
) {
}
