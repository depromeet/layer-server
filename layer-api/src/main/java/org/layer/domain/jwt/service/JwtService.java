package org.layer.domain.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.auth.exception.TokenExceptionType;
import org.layer.domain.jwt.*;
import org.layer.domain.member.entity.MemberRole;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.layer.domain.auth.exception.TokenExceptionType.INVALID_REFRESH_TOKEN;
import static org.layer.config.AuthValueConfig.ACCESS_TOKEN_EXPIRATION_TIME;
import static org.layer.config.AuthValueConfig.REFRESH_TOKEN_EXPIRATION_TIME;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
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

    // Jwt 재발급
    public JwtToken reissueToken(Long memberId) {
        // 리프레시 토큰 검사
        String refreshToken = getRefreshTokenFromRedis(memberId);
        if(!jwtValidator.isValidToken(refreshToken)) {
            throw new BaseCustomException(TokenExceptionType.INVALID_TOKEN); // FIXME: TokenException 등으로 변경 필요
        }


        return issueToken(memberId, getMemberRoleFromRefreshToken(refreshToken));
    }

    private void saveRefreshTokenToRedis(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(memberId.toString(), refreshToken, Duration.ofDays(14));
    }

    private String getRefreshTokenFromRedis(Long memberId) {
        return (String) redisTemplate.opsForValue().get(memberId.toString());
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

    private MemberRole getMemberRoleFromRefreshToken(String refreshToken) {
        MemberRole memberRole = null;
        try {
            List<String> role = jwtValidator.getRoleFromToken(refreshToken);
            memberRole = MemberRole.valueOf(role.get(0));
        } catch(Exception e) {
            throw new BaseCustomException(INVALID_REFRESH_TOKEN);
        }

        return memberRole;
    }
    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(memberId.toString());
    }


}
