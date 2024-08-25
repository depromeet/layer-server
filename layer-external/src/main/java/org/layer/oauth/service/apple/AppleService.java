package org.layer.oauth.service.apple;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.oauth.config.AppleAuthClient;
import org.layer.oauth.dto.service.apple.ApplePublicKeyGenerator;
import org.layer.oauth.dto.service.apple.ApplePublicKeys;
import org.layer.oauth.dto.service.apple.AppleTokenParser;
import org.layer.oauth.exception.OAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.layer.common.exception.TokenExceptionType.INVALID_APPLE_ID_TOKEN;

@RequiredArgsConstructor
@Component
@Slf4j
public class AppleService {

    private final AppleTokenParser appleTokenParser;
    private final AppleAuthClient appleClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final MemberRepository memberRepository;


    private final String DEFAULT_NAME = "apple";
    private final String CLAIM_EMAIL = "email";
    private final String CLAIM_ISSUER = "iss";
    private final String CLAIM_AUDIENCE = "aud";
    private final String CLAIM_SUBJECT = "sub";

    @Value("${apple.login.issuer}")
    private String APPLE_ISSUER;

    @Value("${apple.login.audience}")
    private String APPLE_AUDIENCE;

    @Value("${apple.login.client_id}")
    private String APPLE_CLIENT_ID;

    public void getAppleSocialId(final String appleToken) {
        final Map<String, String> appleTokenHeader = appleTokenParser.parseHeader(appleToken);
        final ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();
        final PublicKey publicKey = applePublicKeyGenerator.generate(appleTokenHeader, applePublicKeys);
        final Claims claims = appleTokenParser.extractClaims(appleToken, publicKey);

        claims.entrySet().stream().forEach(it -> log.info("[line 34] key: {} / value: {}", it.getKey(), it.getValue()));
        log.info("line 58: {}", (String) claims.get(CLAIM_SUBJECT));
        log.info("line 58: {}", (String) claims.get(CLAIM_ISSUER));
        validateIdToken(claims);

        // DB에서 회원 찾기. 없다면 NEED_TO_REGISTER Exception 발생 => 이름 입력 창으로
//        Member member = getMemberBySocialInfoForSignIn((String) claims.get(CLAIM_SUBJECT), SocialType.APPLE);
//        JwtToken jwtToken = issueToken(member.getId(), member.getMemberRole());
//        return SignInResponse.of(member, jwtToken);

        // 있으면 로그인

        // 없으면 throw

    }

//    public Member getMemberBySocialInfoForSignIn(String socialId, SocialType socialType) {
//        return memberRepository.findBySocialIdAndSocialType(socialId, socialType)
//                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.NEED_TO_REGISTER));
//    }

    // id token Claim 검증
    private void validateIdToken(Claims claims) {
        LinkedHashSet<String> auds = (LinkedHashSet<String>)(claims.get(CLAIM_AUDIENCE));

        log.info("line 79: {}", claims.get(CLAIM_ISSUER));
        log.info("line 80: {}", auds);

        // issuer가 apple인지, audience에 layer가 있는지 검증
        if(!claims.get(CLAIM_ISSUER).equals(APPLE_ISSUER)
                || !auds.contains(APPLE_AUDIENCE)) {
            throw new OAuthException(INVALID_APPLE_ID_TOKEN);
        }
    }


}