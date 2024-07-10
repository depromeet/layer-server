package org.layer.domain.auth.controller.dto;

import org.layer.domain.auth.service.dto.ReissueTokenServiceResponse;

public record ReissueTokenResponse(Long memberId, String accessToken, String refreshToken){
    public static ReissueTokenResponse of(ReissueTokenServiceResponse rtsr) {
        return new ReissueTokenResponse(rtsr.memberId(),
                rtsr.jwtToken().getAccessToken(),
                rtsr.jwtToken().getRefreshToken());
    }
}
