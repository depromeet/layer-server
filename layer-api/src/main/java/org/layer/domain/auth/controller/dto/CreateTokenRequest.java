package org.layer.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateTokenRequest", description = "테스트용 토큰 생성 요청 DTO")
public record CreateTokenRequest(
        @NotNull
        @Schema(description = "멤버 ID", example = "1")
        Long memberId
) {
}
