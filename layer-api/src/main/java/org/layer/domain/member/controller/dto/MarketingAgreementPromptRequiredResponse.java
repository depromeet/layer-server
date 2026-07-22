package org.layer.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MarketingAgreementPromptRequiredResponse", description = "마케팅 수신 동의 재요청 팝업 노출 필요 여부 응답 DTO")
public record MarketingAgreementPromptRequiredResponse(
        @Schema(description = "지금 마케팅 동의 팝업을 노출해야 하는지 여부", example = "true")
        boolean promptRequired
) {
}
