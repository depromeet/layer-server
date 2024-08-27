package org.layer.domain.answer.controller.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AnswerListCreateRequest", description = "회고 작성 요청 Dto")
public record AnswerListCreateRequest(
	@Schema(description = "회고 답변 객체 목록", example = "")
	List<AnswerCreateRequest> requests,
	@Schema(description = "임시 저장 여부", example = "true")
	boolean isTemporarySave
) {
}
