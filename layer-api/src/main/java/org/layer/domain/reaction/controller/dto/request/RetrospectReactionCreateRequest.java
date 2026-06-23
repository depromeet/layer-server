package org.layer.domain.reaction.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "RetrospectReactionCreateRequest", description = "회고 반응 생성 요청 DTO")
public record RetrospectReactionCreateRequest(
        @NotBlank
        @Schema(description = "이모지 코드", example = "LEC01")
        String emojiCode,

        @NotNull
        @Schema(description = "답변 ID", example = "1")
        Long answerId
) {
}
