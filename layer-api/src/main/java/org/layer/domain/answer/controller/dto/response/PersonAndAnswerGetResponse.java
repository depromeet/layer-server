package org.layer.domain.answer.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

//@AllArgsConstructor
@Schema(name = "PersonAndAnswerGetResponse", description = "개인-답변 응답 Dto")
public record PersonAndAnswerGetResponse(
	@Schema(description = "답변자", example = "홍길동")
	String name,
	@Schema(description = "탈퇴 여부(Y/N 값이며, Y일때 탈퇴한 회원)", example = "N")
	String delYn,
	@Schema(description = "답변 내용", example = "답변 내용입니당")
	String answerContent
) {
}
