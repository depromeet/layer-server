package org.layer.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.member.entity.AgreementType;
import org.layer.domain.member.entity.MemberAgreement;

import java.time.LocalDateTime;

@Schema(name = "MemberAgreementResponse", description = "약관 동의 상태 응답 DTO")
public record MemberAgreementResponse(
        @Schema(description = "약관 종류", example = "MARKETING")
        AgreementType agreementType,

        @Schema(description = "동의 여부", example = "true")
        boolean agreed,

        @Schema(description = "동의한 시각 (미동의 상태면 null)", example = "2026-07-22T10:00:00")
        LocalDateTime agreedAt
) {
    public static MemberAgreementResponse from(MemberAgreement memberAgreement) {
        return new MemberAgreementResponse(
                memberAgreement.getAgreementType(),
                memberAgreement.isAgreed(),
                memberAgreement.getAgreedAt()
        );
    }
}
