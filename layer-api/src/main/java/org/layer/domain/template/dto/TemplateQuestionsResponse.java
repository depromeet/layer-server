package org.layer.domain.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record TemplateQuestionsResponse(
        @Schema(description = "템플릿 ID")
        @NotNull
        Long id,
        @Schema(description = "템플릿 제목", example = "빠르고 효율적인 회고")
        @NotNull
        String title,
        @Schema(description = "템플릿 명칭", example = "KPT 회고")
        @NotNull
        String templateName,
        @Schema(description = "템플릿에 속한 질문 리스트")
        List<String> questionList
) {
}
