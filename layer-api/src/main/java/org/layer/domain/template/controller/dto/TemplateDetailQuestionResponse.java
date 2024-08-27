package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TemplateDetailQuestionResponse(
        @Schema(description = "질문 id", example = "1")
        Long questionId,
        @Schema(description = "질문", example = "어려움을 느꼈던 부분은 무엇인가요?")
        String question,
        @Schema(description = "질문에 대한 상세 설명", example = "현재 만족하고 있거나 계속 이어갔으면 하는 부분들을 작성해요.")
        String description
) {
}
