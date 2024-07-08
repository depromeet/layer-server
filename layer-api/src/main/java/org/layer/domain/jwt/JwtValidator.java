package org.layer.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.jwt.exception.TokenException;
import org.springframework.stereotype.Component;

import java.util.List;

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
        Claims claims = getClaims(token);
        return Long.parseLong(claims.get("memberId").toString());
    }

    public List<String> getRoleFromToken(String token) throws TokenException {
        Claims claims = getClaims(token);
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
