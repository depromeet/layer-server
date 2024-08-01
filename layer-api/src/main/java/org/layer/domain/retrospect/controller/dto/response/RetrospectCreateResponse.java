package org.layer.domain.retrospect.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RetrospectCreateResponse", description = "회고 생성 응답 DTO")
public record RetrospectCreateResponse(
        @Schema(description = "회고 id", example = "1")
        Long retrospectId
) {
    public static RetrospectCreateResponse of(Long retrospectId) {

        return new RetrospectCreateResponse(retrospectId);
    }
}
