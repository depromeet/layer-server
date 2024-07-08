package org.layer.domain.jwt.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.jwt.JwtProvider;
import org.layer.domain.jwt.JwtToken;
import org.layer.domain.jwt.MemberAuthentication;
import org.layer.domain.jwt.exception.TokenException;
import org.layer.domain.member.entity.MemberRole;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

import static org.layer.config.AuthValueConfig.ACCESS_TOKEN_EXPIRATION_TIME;
import static org.layer.config.AuthValueConfig.REFRESH_TOKEN_EXPIRATION_TIME;

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

    private Long getMemberIdFromRefreshToken(String refreshToken) throws TokenException {
        Long memberId = null;
        try {
            memberId = Long.parseLong((String) Objects.requireNonNull(redisTemplate.opsForValue().get(refreshToken)));
        } catch(Exception e) {
            throw new TokenException();
        }
        return memberId;
    }

    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(memberId.toString());
    }

}
