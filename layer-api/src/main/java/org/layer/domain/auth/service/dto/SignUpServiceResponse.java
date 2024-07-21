package org.layer.domain.auth.service.dto;

import org.layer.domain.jwt.JwtToken;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;

public record SignUpServiceResponse(Long memberId,
                                    String name,
                                    String email,
                                    MemberRole memberRole,
                                    String socialId,
                                    SocialType socialType,
                                    String accessToken,
                                    String refreshToken) {
    public static SignUpServiceResponse of(Member member, JwtToken jwtToken) {
        return new SignUpServiceResponse(member.getId(),
                member.getName(),
                member.getEmail(),
                member.getMemberRole(),
                member.getSocialId(),
                member.getSocialType(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken());
    }
}
