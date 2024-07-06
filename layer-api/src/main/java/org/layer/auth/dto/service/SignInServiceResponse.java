package org.layer.auth.dto.service;

import org.layer.auth.jwt.JwtToken;
import org.layer.member.Member;
import org.layer.member.MemberRole;

public record SignInServiceResponse(Long memberId, String accessToken, String refreshToken, MemberRole memberRole) {
    public static SignInServiceResponse of(Member member, JwtToken jwtToken) {
        return new SignInServiceResponse(member.getId(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken(),
                member.getMemberRole());
    }
}
