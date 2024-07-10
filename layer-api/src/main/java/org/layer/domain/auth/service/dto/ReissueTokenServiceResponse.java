package org.layer.domain.auth.service.dto;

import org.layer.domain.jwt.JwtToken;
import org.layer.domain.member.entity.Member;

public record ReissueTokenServiceResponse(Long memberId, JwtToken jwtToken) {
    public static ReissueTokenServiceResponse of(Member member, JwtToken jwtToken) {
        return new ReissueTokenServiceResponse(member.getId(), jwtToken);
    }
}