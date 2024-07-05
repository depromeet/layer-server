package org.layer.auth.service;

import lombok.RequiredArgsConstructor;
import org.layer.auth.exception.TokenException;
import org.layer.auth.jwt.*;
import org.layer.config.AuthValueConfig;
import org.layer.member.MemberRole;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

import static org.layer.config.AuthValueConfig.*;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtToken issueToken(Long memberId, MemberRole memberRole) {
        String accessToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), REFRESH_TOKEN_EXPIRATION_TIME);

        saveRefreshTokenToRedis(memberId, memberRole, refreshToken);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveRefreshTokenToRedis(Long memberId, MemberRole memberRole, String refreshToken) {
        redisTemplate.opsForValue().set(refreshToken, memberId, Duration.ofDays(14));
    }

    private Long getMemberIdFromRefreshToken(String refreshToken) throws TokenException {
        Long memberId = null;
        try {
            memberId = Long.parseLong((String) Objects.requireNonNull(redisTemplate.opsForValue().get(refreshToken)));
        } catch(Exception e) {
            throw new TokenException();
        }
        return memberId;
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

}
