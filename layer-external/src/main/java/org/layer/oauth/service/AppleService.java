package org.layer.oauth.service;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.oauth.config.AppleOAuthConfig;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppleService {
    private final AppleOAuthConfig appleOAuthConfig;
    public String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        log.info("issuer: {}", appleOAuthConfig.getIssuer());
        log.info("audience: {}", appleOAuthConfig.getAudience());
        log.info("clientId: {}", appleOAuthConfig.getClientId());


        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleOAuthConfig.getKeyId())
                .setIssuer(appleOAuthConfig.getIssuer())
                .setAudience(appleOAuthConfig.getAudience())
                .setSubject(appleOAuthConfig.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
//                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

//    private PrivateKey getPrivateKey() {
//
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
//
//        try {
//            byte[] privateKeyBytes = Base64.getDecoder().decode(appleOAuthConfig.getPrivateKey());
//
//            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
//            return converter.getPrivateKey(privateKeyInfo);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting private key from String", e);
//        }
//    }

}
