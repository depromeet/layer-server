package org.layer.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.layer.annotation.MemberId;
import org.layer.domain.member.controller.dto.MarketingAgreementPromptRequiredResponse;
import org.layer.domain.member.controller.dto.MemberAgreementResponse;
import org.layer.domain.member.controller.dto.MemberAgreementsGetResponse;
import org.layer.domain.member.controller.dto.UpdateMarketingAgreementRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = "약관 동의", description = "이용약관/개인정보/마케팅 동의 관련 API")
public interface MemberAgreementApi {

    @Operation(summary = "약관 동의 상태 전체 조회", description = "설정 화면 등에서 회원의 약관별(이용약관/개인정보/마케팅) 동의 상태를 조회합니다. 마케팅은 한 번도 응답하지 않았다면 목록에서 빠집니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MemberAgreementsGetResponse.class))
            })
    })
    ResponseEntity<MemberAgreementsGetResponse> getAgreements(@MemberId Long memberId);

    @Operation(summary = "마케팅 동의 재요청 필요 여부 조회", description = "마케팅 동의를 한 번도 안 했거나, 거절 후 재요청 주기(기본 90일)가 지난 회원에게 동의 팝업을 다시 띄워야 하는지 확인합니다. 프론트는 앱 진입 시 이 API로 확인 후 true면 동의 팝업을 노출합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MarketingAgreementPromptRequiredResponse.class))
            })
    })
    ResponseEntity<MarketingAgreementPromptRequiredResponse> isMarketingPromptRequired(@MemberId Long memberId);

    @Operation(summary = "마케팅 동의 상태 변경", description = "마케팅 활용 및 광고 수신 동의 여부를 저장합니다. 설정 화면의 on/off 토글과, 동의 재요청 팝업에 대한 응답 저장에 공통으로 사용합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MemberAgreementResponse.class))
            })
    })
    ResponseEntity<MemberAgreementResponse> updateMarketingAgreement(
            @MemberId Long memberId,
            @Valid UpdateMarketingAgreementRequest request
    );
}
