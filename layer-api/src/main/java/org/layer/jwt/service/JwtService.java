package org.layer.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.jwt.JwtProvider;
import org.layer.jwt.JwtToken;
import org.layer.jwt.JwtValidator;
import org.layer.jwt.MemberAuthentication;
import org.layer.jwt.exception.TokenException;
import org.layer.domain.member.entity.MemberRole;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

import static org.layer.config.AuthValueConfig.ACCESS_TOKEN_EXPIRATION_TIME;
import static org.layer.config.AuthValueConfig.REFRESH_TOKEN_EXPIRATION_TIME;
import static org.layer.global.exception.ApiTokenExceptionType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtToken issueToken(Long memberId, MemberRole memberRole) {
        String accessToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtProvider.createToken(MemberAuthentication.create(memberId, memberRole), REFRESH_TOKEN_EXPIRATION_TIME);

        saveRefreshTokenToRedis(memberId, refreshToken);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Jwt 재발급
    public JwtToken reissueToken(String requestRefreshToken, Long memberId) {
        // 리프레시 토큰 검사
        String refreshToken = getRefreshTokenFromRedis(memberId);

        // 1. requestRefreshToken이 null이거나 empty
        if(requestRefreshToken == null || requestRefreshToken.isEmpty()) {
            throw new TokenException(NO_REFRESH_TOKEN);
        }

        // 2. 요청 헤더로 들어온 것과 같은가
        if(!refreshToken.equals(requestRefreshToken)) {
            throw new TokenException(INVALID_REFRESH_TOKEN);
        }

        // 3. 파싱했을 때 유효한가
        if(!jwtValidator.isValidToken(refreshToken)) {
            throw new TokenException(INVALID_TOKEN);
        }

        return issueToken(memberId, getMemberRoleFromRefreshToken(refreshToken));
    }

    private void saveRefreshTokenToRedis(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(memberId.toString(), refreshToken, Duration.ofDays(14));
    }

    private String getRefreshTokenFromRedis(Long memberId) {
        return redisTemplate.opsForValue().get(memberId.toString());
    }

    private MemberRole getMemberRoleFromRefreshToken(String refreshToken) {
        try {
            List<String> role = jwtValidator.getRoleFromToken(refreshToken);
            MemberRole memberRole = MemberRole.valueOf(role.get(0));
            return memberRole;
        } catch(Exception e) {
            throw new TokenException(INVALID_REFRESH_TOKEN);
        }
    }
    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(memberId.toString());
    }
}
