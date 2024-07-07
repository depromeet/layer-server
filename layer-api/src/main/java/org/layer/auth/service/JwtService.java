package org.layer.auth.service;

import lombok.RequiredArgsConstructor;
import org.layer.auth.jwt.*;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.member.entity.MemberRole;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

import static org.layer.auth.exception.TokenExceptionType.INVALID_REFRESH_TOKEN;
import static org.layer.config.AuthValueConfig.*;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtToken issueToken(Long memberId, MemberRole memberRole) {
        String accessToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), REFRESH_TOKEN_EXPIRATION_TIME);

        saveRefreshTokenToRedis(memberId, refreshToken);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveRefreshTokenToRedis(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(memberId.toString(), refreshToken, Duration.ofDays(14));
    }

    private Long getMemberIdFromRefreshToken(String refreshToken) {
        Long memberId = null;
        try {
            memberId = Long.parseLong((String) Objects.requireNonNull(redisTemplate.opsForValue().get(refreshToken)));
        } catch(Exception e) {
            throw new BaseCustomException(INVALID_REFRESH_TOKEN);
        }
        return memberId;
    }

    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(memberId.toString());
    }

}
