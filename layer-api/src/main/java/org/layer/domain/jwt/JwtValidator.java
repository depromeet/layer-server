package org.layer.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.exception.BaseCustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.layer.domain.auth.exception.TokenExceptionType.INVALID_TOKEN;
import static org.layer.domain.jwt.JwtValidationType.INVALID_JWT;
import static org.layer.domain.jwt.JwtValidationType.VALID_JWT;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtValidator {
    private final SecretKeyFactory secretKeyFactory;

    public JwtValidationType validateToken(String token) {
        try {
            getClaims(token);
            return VALID_JWT;
        } catch(Exception e) {
            return INVALID_JWT;
        }
    }

    public long getMemberIdFromToken(String token) {
        Claims claims;
        try {
            claims = getClaims(token);
        } catch(Exception e) {
            throw new BaseCustomException(INVALID_TOKEN);
        }
        return Long.parseLong(claims.get("memberId").toString());
    }

    public List<String> getRoleFromToken(String token) {
        Claims claims;
        try {
            claims = getClaims(token);
        } catch(Exception e) {
            throw new BaseCustomException(INVALID_TOKEN);
        }
        return (List<String>) (claims.get("role"));
    }

    // 정상적인 토큰인지 판단하는 메서드
    public boolean isValidToken(String token) {
        return StringUtils.hasText(token) && validateToken(token) == JwtValidationType.VALID_JWT;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKeyFactory.createSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
