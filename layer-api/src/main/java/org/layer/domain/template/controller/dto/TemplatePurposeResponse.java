package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.template.entity.TemplatePurpose;

@Builder
@Schema(description = "템플릿 목적 응답 ")
public record TemplatePurposeResponse(
        @Schema(description = "기본 ID")
        @NotNull
        Long id,
        @Schema(description = "템플릿 목적", example = "빠르고 효율적인 회고")
        @NotNull
        String purpose
) {
    public static TemplatePurposeResponse toResponse(TemplatePurpose purpose) {
        return TemplatePurposeResponse.builder()
                .id(purpose.getId())
                .purpose(purpose.getPurpose())
                .build();
    }
}
