package org.layer.auth.dto.controller;

import org.layer.auth.dto.service.SignInServiceResponse;
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
