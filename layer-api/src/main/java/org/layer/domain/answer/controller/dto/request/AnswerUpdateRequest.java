package org.layer.domain.answer.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회고 답변 수정 요청 Dto")
public record AnswerUpdateRequest(
        @Schema(description = "질문 id", example = "1")
        Long questionId,
        @Schema(description = "질문 타입", example = "plain_text")
        String questionType,
        @Schema(description = "질문에 대한 답변", example = "깊은 고민을 할 수 있어서 좋았어요.")
        String answerContent
) {
}
