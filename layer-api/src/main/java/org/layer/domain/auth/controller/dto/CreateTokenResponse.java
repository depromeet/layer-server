package org.layer.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateTokenResponse", description = "테스트용 토큰 생성 응답 DTO")
public record CreateTokenResponse(
        @Schema(description = "액세스 토큰 (100년 유효)")
        String accessToken
) {
}
