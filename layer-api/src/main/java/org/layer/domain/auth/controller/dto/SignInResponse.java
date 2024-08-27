package org.layer.domain.auth.controller.dto;

import lombok.Builder;
import org.layer.domain.jwt.JwtToken;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;

@Builder
public record SignInResponse(Long memberId,
                             String name,
                             String email,
                             MemberRole memberRole,
                             String socialId,
                             SocialType socialType,
                             String accessToken,
                             String refreshToken,
                             String imageUrl) {
    public static SignInResponse of(Member member, JwtToken jwtToken) {
        return SignInResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .memberRole(member.getMemberRole())
                .socialType(member.getSocialType())
                .socialId(member.getSocialId())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .imageUrl(member.getProfileImageUrl())
                .build();
    }
}
