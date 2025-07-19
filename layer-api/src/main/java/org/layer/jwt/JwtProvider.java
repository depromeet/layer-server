package org.layer.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private final SecretKeyFactory secretKeyFactory;

    public String createToken(Authentication authentication, Long tokenExpirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", authentication.getPrincipal());
        claims.put("role", authentication.getAuthorities());

        Date now = new Date();

        return Jwts.builder()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenExpirationTime))
                .claims(claims)
                .signWith(secretKeyFactory.createSecretKey())
                .compact();
    }
}
