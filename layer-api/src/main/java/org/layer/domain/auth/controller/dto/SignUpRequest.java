package org.layer.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.member.entity.SocialType;

public record SignUpRequest(
        SocialType socialType,
        String name,

        @Schema(description = "이용약관 동의 여부 (필수)", example = "true")
        Boolean termsAgreed,

        @Schema(description = "개인정보 수집 및 이용 동의 여부 (필수)", example = "true")
        Boolean privacyAgreed,

        @Schema(description = "마케팅 활용 및 광고 수신 동의 여부 (선택, 미응답 시 null)", example = "false")
        Boolean marketingAgreed
) {
    // 프론트가 아직 이 필드들을 안 보내는 과도기 대응: 필드 자체가 없으면(null) 기존 방식대로 동의한 것으로 간주.
    // 프론트가 명시적으로 false를 보낸 경우는 그대로 거부 대상이라 구분해서 처리한다.
    // 프론트 배포가 끝나면 이 기본값 처리는 제거하고 필수값으로 강제해야 한다.
    public boolean isTermsAgreedOrDefault() {
        return termsAgreed == null || termsAgreed;
    }

    public boolean isPrivacyAgreedOrDefault() {
        return privacyAgreed == null || privacyAgreed;
    }
}
