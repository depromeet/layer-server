package org.layer.domain.answer.controller.dto.response;

import java.util.List;

public record AnswerByQuestionGetResponse(
	String questionContent,
	String questionType,
	List<PersonAndAnswerGetResponse> answers
) {
}
