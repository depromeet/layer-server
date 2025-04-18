package org.layer.oauth.service;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.layer.global.exception.TokenExceptionType;
import org.layer.oauth.config.AppleAuthClient;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.dto.service.apple.ApplePublicKeyGenerator;
import org.layer.oauth.dto.service.apple.ApplePublicKeys;
import org.layer.oauth.dto.service.apple.AppleTokenParser;
import org.layer.oauth.exception.OAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.layer.domain.member.entity.SocialType.APPLE;
import static org.layer.global.exception.TokenExceptionType.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class AppleService implements OAuthService {

    private final AppleTokenParser appleTokenParser;
    private final AppleAuthClient appleClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final String CLAIM_EMAIL = "email";
    private final String CLAIM_ISSUER = "iss";
    private final String CLAIM_AUDIENCE = "aud";
    private final String CLAIM_SUBJECT = "sub";

    @Value("${apple.login.issuer}")
    private String APPLE_ISSUER;

    @Value("${apple.login.audience}")
    private String APPLE_AUDIENCE;

    public MemberInfoServiceResponse getMemberInfo(final String appleToken) {
        final Map<String, String> appleTokenHeader = appleTokenParser.parseHeader(appleToken);
        final ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();
        final PublicKey publicKey = applePublicKeyGenerator.generate(appleTokenHeader, applePublicKeys);
        final Claims claims = appleTokenParser.extractClaims(appleToken, publicKey);

        validateIdToken(claims);

        return new MemberInfoServiceResponse((String) claims.get(CLAIM_SUBJECT), APPLE, (String) claims.get(CLAIM_EMAIL));

    }

    // id token Claim 검증
    private void validateIdToken(Claims claims) {
        LinkedHashSet<String> auds = (LinkedHashSet<String>)(claims.get(CLAIM_AUDIENCE));

        // issuer가 apple인지, audience에 layer가 있는지 검증
        if(!claims.get(CLAIM_ISSUER).equals(APPLE_ISSUER)
                || !auds.contains(APPLE_AUDIENCE)) {
            throw new OAuthException(INVALID_APPLE_ID_TOKEN);
        }
    }


}