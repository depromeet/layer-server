package org.layer.domain.auth.service.dto;

import org.layer.domain.jwt.JwtToken;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;

public record SignInServiceResponse(Long memberId, String accessToken, String refreshToken, MemberRole memberRole) {
    public static SignInServiceResponse of(Member member, JwtToken jwtToken) {
        return new SignInServiceResponse(member.getId(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken(),
                member.getMemberRole());
    }
}
