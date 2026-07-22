package org.layer.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.member.entity.MemberAgreement;

import java.util.List;

@Schema(name = "MemberAgreementsGetResponse", description = "약관 전체 동의 상태 조회 응답 DTO")
public record MemberAgreementsGetResponse(
        @Schema(description = "약관별 동의 상태 목록. 마케팅은 아직 한 번도 응답하지 않았다면 목록에 포함되지 않음")
        List<MemberAgreementResponse> agreements
) {
    public static MemberAgreementsGetResponse from(List<MemberAgreement> memberAgreements) {
        List<MemberAgreementResponse> responses = memberAgreements.stream()
                .map(MemberAgreementResponse::from)
                .toList();
        return new MemberAgreementsGetResponse(responses);
    }
}
