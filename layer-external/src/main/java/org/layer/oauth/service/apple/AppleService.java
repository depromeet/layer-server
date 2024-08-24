package org.layer.oauth.service.apple;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.oauth.config.AppleAuthClient;
import org.layer.oauth.dto.service.apple.ApplePublicKeyGenerator;
import org.layer.oauth.dto.service.apple.ApplePublicKeys;
import org.layer.oauth.dto.service.apple.AppleTokenParser;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class AppleService {

    private final AppleTokenParser appleTokenParser;
    private final AppleAuthClient appleClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;


    private final String DEFAULT_NAME = "apple";
    private final String CLAIM_EMAIL = "email";

    public void createAppleUser(final String appleToken) {
        final Map<String, String> appleTokenHeader = appleTokenParser.parseHeader(appleToken);
        final ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();
        final PublicKey publicKey = applePublicKeyGenerator.generate(appleTokenHeader, applePublicKeys);
        final Claims claims = appleTokenParser.extractClaims(appleToken, publicKey);
        claims.values().forEach(it -> {
            log.info("{}<", it);
        });
        return;
    }
}