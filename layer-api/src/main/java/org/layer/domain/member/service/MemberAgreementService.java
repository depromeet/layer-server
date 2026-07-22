package org.layer.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.common.time.Time;
import org.layer.domain.member.controller.dto.MarketingAgreementPromptRequiredResponse;
import org.layer.domain.member.controller.dto.MemberAgreementResponse;
import org.layer.domain.member.controller.dto.MemberAgreementsGetResponse;
import org.layer.domain.member.controller.dto.UpdateMarketingAgreementRequest;
import org.layer.domain.member.entity.MemberAgreement;
import org.layer.domain.member.repository.MemberAgreementRepository;
import org.layer.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.layer.domain.member.entity.AgreementType.MARKETING;

@RequiredArgsConstructor
@Service
public class MemberAgreementService {
    private final MemberAgreementRepository memberAgreementRepository;
    private final MemberRepository memberRepository;
    private final Time time;

    // 마케팅 수신 동의를 거절(또는 미응답)한 회원에게 다시 물어볼 때까지의 최소 간격(일)
    @Value("${member.agreement.marketing-reprompt-interval-days}")
    private long marketingRepromptIntervalDays;

    @Transactional(readOnly = true)
    public MemberAgreementsGetResponse getAgreements(Long memberId) {
        memberRepository.findValidMemberByIdOrThrow(memberId);
        List<MemberAgreement> agreements = memberAgreementRepository.findAllByMemberId(memberId);
        return MemberAgreementsGetResponse.from(agreements);
    }

    @Transactional(readOnly = true)
    public MarketingAgreementPromptRequiredResponse isMarketingPromptRequired(Long memberId) {
        memberRepository.findValidMemberByIdOrThrow(memberId);

        Optional<MemberAgreement> marketingAgreement =
                memberAgreementRepository.findByMemberIdAndAgreementType(memberId, MARKETING);

        // 한 번도 물어본 적 없는 회원(기존 유저 포함)은 항상 노출 대상
        boolean promptRequired = marketingAgreement
                .map(this::needsReprompt)
                .orElse(true);

        return new MarketingAgreementPromptRequiredResponse(promptRequired);
    }

    @Transactional
    public MemberAgreementResponse updateMarketingAgreement(Long memberId, UpdateMarketingAgreementRequest request) {
        memberRepository.findValidMemberByIdOrThrow(memberId);
        LocalDateTime now = time.now();

        MemberAgreement agreement = memberAgreementRepository
                .findByMemberIdAndAgreementType(memberId, MARKETING)
                .orElseGet(() -> memberAgreementRepository.save(
                        MemberAgreement.builder()
                                .memberId(memberId)
                                .agreementType(MARKETING)
                                .agreed(false)
                                .lastAskedAt(now)
                                .build()
                ));

        agreement.updateAgreement(request.agreed(), now);

        return MemberAgreementResponse.from(agreement);
    }

    private boolean needsReprompt(MemberAgreement agreement) {
        if (agreement.isAgreed()) {
            return false;
        }
        LocalDateTime repromptDeadline = agreement.getLastAskedAt().plusDays(marketingRepromptIntervalDays);
        return !time.now().isBefore(repromptDeadline);
    }
}
