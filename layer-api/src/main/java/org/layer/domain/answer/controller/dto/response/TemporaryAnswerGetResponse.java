package org.layer.domain.answer.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TemporaryAnswerGetResponse", description = "임시 회고 조회 응답 Dto")
public record TemporaryAnswerGetResponse(
	@Schema(description = "질문 id", example = "1")
	Long questionId,
	@Schema(description = "질문 타입", example = "plain_text")
	String questionType,
	@Schema(description = "질문에 대한 답변", example = "깊은 고민을 할 수 있어서 좋았어요.")
	String answerContent
	) {

	public static TemporaryAnswerGetResponse of(Long questionId, String questionType, String answerContent){
		return new TemporaryAnswerGetResponse(questionId, questionType, answerContent);
	}
}
