package org.layer.auth.dto.controller;

import org.layer.auth.dto.service.ReissueTokenServiceResponse;

public record ReissueTokenResponse(Long memberId, String accessToken, String refreshToken){
    public static ReissueTokenResponse of(ReissueTokenServiceResponse rtsr) {
        return new ReissueTokenResponse(rtsr.memberId(),
                rtsr.jwtToken().getAccessToken(),
                rtsr.jwtToken().getRefreshToken());
    }
}
