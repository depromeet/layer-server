package org.layer.domain.answer.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "회고 답변 수정 Dto")
public record AnswerListUpdateRequest(
        @Schema(description = "회고 답변 객체 목록")
        List<AnswerUpdateRequest> requests
) {
}
