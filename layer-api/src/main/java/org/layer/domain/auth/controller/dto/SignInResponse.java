package org.layer.domain.auth.controller.dto;

import org.layer.domain.auth.service.dto.SignInServiceResponse;
import org.layer.domain.member.entity.MemberRole;


public record SignInResponse(Long memberId, String accessToken, String refreshToken, MemberRole memberRole) {
    public static SignInResponse of(SignInServiceResponse signInServiceResponse) {
        return new SignInResponse(signInServiceResponse.memberId(),
                signInServiceResponse.accessToken(),
                signInServiceResponse.refreshToken(),
                signInServiceResponse.memberRole()
        );
    }
}
