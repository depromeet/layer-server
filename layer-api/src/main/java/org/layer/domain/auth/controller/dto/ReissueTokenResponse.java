package org.layer.domain.auth.controller.dto;

import lombok.Builder;
import org.layer.domain.auth.service.dto.ReissueTokenServiceResponse;
import org.layer.domain.jwt.JwtToken;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;

@Builder
public record ReissueTokenResponse(Long memberId,
                                   String name,
                                   String email,
                                   MemberRole memberRole,
                                   String socialId,
                                   SocialType socialType,
                                   String accessToken,
                                   String refreshToken){
    public static ReissueTokenResponse of(ReissueTokenServiceResponse rtsr) {
        Member member = rtsr.member();
        JwtToken jwtToken = rtsr.jwtToken();
        return ReissueTokenResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .memberRole(member.getMemberRole())
                .socialType(member.getSocialType())
                .socialId(member.getSocialId())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
