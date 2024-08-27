package org.layer.domain.answer.controller.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AnswerByQuestionGetResponse", description = "임시 회고 조회 응답 Dto")
public record AnswerByQuestionGetResponse(
	@Schema(description = "질문 내용", example = "질문 내용입니당")
	String questionContent,
	@Schema(description = "질문 타입", example = "plain_text")
	String questionType,
	@Schema(description = "질문 객체", example = "")
	List<PersonAndAnswerGetResponse> answers
) {
}
