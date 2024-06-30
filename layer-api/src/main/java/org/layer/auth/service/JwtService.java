package org.layer.auth.service;

import lombok.RequiredArgsConstructor;
import org.layer.auth.dao.RefreshTokenRepository;
import org.layer.auth.jwt.*;
import org.layer.config.AuthValueConfig;
import org.layer.member.MemberRole;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.layer.config.AuthValueConfig.*;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtToken issueToken(Long memberId, MemberRole memberRole) {
        String accessToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), REFRESH_TOKEN_EXPIRATION_TIME);

        saveRefreshTokenToRedis(memberId, memberRole, refreshToken);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    private void saveRefreshTokenToRedis(Long memberId, MemberRole memberRole, String tokenString) {
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(memberId)
                .token(tokenString)
                .ttl(REFRESH_TOKEN_EXPIRATION_TIME)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

}
