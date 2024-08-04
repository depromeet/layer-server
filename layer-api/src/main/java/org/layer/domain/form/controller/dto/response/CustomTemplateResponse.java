package org.layer.domain.form.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(name = "DefaultFormGetResponse", description = "커스텀 템플릿 조회 간단 정보 응답 Dto")
public record CustomTemplateResponse(
        @NotNull
        @Schema(description = "회고폼 ID", example = "1")
        Long id,
        @NotNull
        @Schema(description = "회고폼 이름", example = "Mad Sad Glad 커스텀 템플릿")
        String title,
        @NotNull
        @Schema(description = "태그", example = "Mad Sad Glad")
        String formTag,
        @NotNull
        @Schema(description = "폼 생성일자", example = "2024-08-03T21:40:52.880535")
        LocalDateTime createdAt
) {
}
