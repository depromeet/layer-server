package org.layer.oauth.service;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;

import java.time.LocalDateTime;

public class AppleService {
    private String generateClientSecret() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
                .setIssuer(appleProperties.getTeamId())
                .setAudience(appleProperties.getAudience())
                .setSubject(appleProperties.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }
}
