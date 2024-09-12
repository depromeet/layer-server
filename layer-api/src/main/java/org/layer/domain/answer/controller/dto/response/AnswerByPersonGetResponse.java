package org.layer.domain.answer.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "AnswerByPersonGetResponse", description = "개인별 답변 조회 응답 Dto")
public record AnswerByPersonGetResponse(
	@Schema(description = "답변자", example = "홍길동")
	String name,
	@Schema(description = "탈퇴 여부. 탈퇴시 true", example = "false")
	Boolean deleted,
	@Schema(description = "질문-답변 객체", example = "")
	List<QuestionAndAnswerGetResponse> answers
) {
}
