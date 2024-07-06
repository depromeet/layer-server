package org.layer.auth.dto.service;

import org.layer.auth.jwt.JwtToken;
import org.layer.member.Member;

public record ReissueTokenServiceResponse(Long memberId, JwtToken jwtToken) {
    public static ReissueTokenServiceResponse of(Member member, JwtToken jwtToken) {
        return new ReissueTokenServiceResponse(member.getId(), jwtToken);
    }
}
