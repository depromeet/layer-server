package org.layer.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.layer.annotation.MemberId;
import org.layer.domain.member.controller.dto.MarketingAgreementPromptRequiredResponse;
import org.layer.domain.member.controller.dto.MemberAgreementResponse;
import org.layer.domain.member.controller.dto.MemberAgreementsGetResponse;
import org.layer.domain.member.controller.dto.UpdateMarketingAgreementRequest;
import org.layer.domain.member.service.MemberAgreementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member/agreements")
@RequiredArgsConstructor
@RestController
public class MemberAgreementController implements MemberAgreementApi {
    private final MemberAgreementService memberAgreementService;

    @Override
    @GetMapping
    public ResponseEntity<MemberAgreementsGetResponse> getAgreements(@MemberId Long memberId) {
        return ResponseEntity.ok(memberAgreementService.getAgreements(memberId));
    }

    @Override
    @GetMapping("/marketing/prompt-required")
    public ResponseEntity<MarketingAgreementPromptRequiredResponse> isMarketingPromptRequired(@MemberId Long memberId) {
        return ResponseEntity.ok(memberAgreementService.isMarketingPromptRequired(memberId));
    }

    @Override
    @PatchMapping("/marketing")
    public ResponseEntity<MemberAgreementResponse> updateMarketingAgreement(
            @MemberId Long memberId,
            @Valid @RequestBody UpdateMarketingAgreementRequest request
    ) {
        return ResponseEntity.ok(memberAgreementService.updateMarketingAgreement(memberId, request));
    }
}
