package org.layer.oauth.service;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.layer.oauth.config.AppleOAuthConfig;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.util.Base64.getEncoder;

@RequiredArgsConstructor
@Service
public class AppleService {
    private final AppleOAuthConfig appleOAuthConfig;
    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleOAuthConfig.getKeyId())
                .setIssuer(appleOAuthConfig.getIssuer())
                .setAudience(appleOAuthConfig.getAudience())
                .setSubject(appleOAuthConfig.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(createAppleSecretKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    SecretKey createAppleSecretKey() {
        String encodedKey = getEncoder().encodeToString(appleOAuthConfig.getSecretKey().getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }
}
