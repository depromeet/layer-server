package org.layer.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.auth.exception.TokenExceptionType;
import org.layer.common.exception.BaseCustomException;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

import static org.layer.auth.exception.TokenExceptionType.*;
import static org.layer.auth.jwt.JwtValidationType.*;

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

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKeyFactory.createSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
