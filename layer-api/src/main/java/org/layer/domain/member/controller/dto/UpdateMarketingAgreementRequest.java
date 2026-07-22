package org.layer.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UpdateMarketingAgreementRequest", description = "마케팅 활용 및 광고 수신 동의 변경 요청 DTO")
public record UpdateMarketingAgreementRequest(
        @NotNull
        @Schema(description = "마케팅 수신 동의 여부", example = "true")
        Boolean agreed
) {
}
