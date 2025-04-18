package org.layer.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.oauth.dto.service.kakao.KakaoGetMemberInfoServiceResponse;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.exception.OAuthException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.layer.domain.member.entity.SocialType.KAKAO;
import static org.layer.global.exception.AuthExceptionType.*;
import static org.layer.global.exception.MemberExceptionType.*;
import static org.layer.oauth.config.KakaoOAuthConfig.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService implements OAuthService {
    private final RestClient restClient;

    public MemberInfoServiceResponse getMemberInfo(final String accessToken) {
        KakaoGetMemberInfoServiceResponse response = null;
        try {
            response = restClient.get()
                    .uri(KAKAO_URI)
                    .header(AUTHORIZATION, TOKEN_PREFIX + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (kakaoRequest, kakaoResponse) -> {
                                throw new OAuthException(FAIL_TO_AUTH);
                            })
                    .body(KakaoGetMemberInfoServiceResponse.class);
        } catch(Exception e) {
            throw new OAuthException(FAIL_TO_AUTH);
        }

        assert response != null;
        return new MemberInfoServiceResponse(response.id(), KAKAO, response.kakao_account().email());
    }
}
