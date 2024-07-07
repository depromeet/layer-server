package org.layer.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.exception.BaseCustomException;
import org.layer.oauth.config.KakaoOAuthConfig;
import org.layer.oauth.dto.service.kakao.KakaoGetMemberInfoServiceResponse;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.layer.common.exception.MemberExceptionType.FAIL_TO_AUTH;
import static org.layer.domain.member.entity.SocialType.KAKAO;
import static org.layer.oauth.config.KakaoOAuthConfig.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {
    private final KakaoOAuthConfig kakaoOAuthConfig;

    public MemberInfoServiceResponse getMemberInfo(final String accessToken) {
        KakaoGetMemberInfoServiceResponse response = null;
        try {
            RestClient restClient = RestClient.create();
            response = restClient.get()
                    .uri(KAKAO_URI)
                    .header(AUTHORIZATION, TOKEN_PREFIX + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (kakaoRequest, kakaoResponse) -> {
                                throw new BaseCustomException(FAIL_TO_AUTH);
                            })
                    .body(KakaoGetMemberInfoServiceResponse.class);
        } catch(Exception e) {
            throw new BaseCustomException(FAIL_TO_AUTH);
        }

        assert response != null;
        return new MemberInfoServiceResponse(response.id(), KAKAO, response.kakao_account().email());
    }

    //== 이건 프론트에서..? TODO: 지우기 ==//
    public String getToken(String code) {
        // 토큰 요청 데이터
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthConfig.getKakaoLoginApiKey());
        params.add("redirect_uri", kakaoOAuthConfig.getRedirectUri());
        params.add("code", code);


        Map response = RestClient.create(kakaoOAuthConfig.getKakaoAuthBaseUri())
                .post()
                .uri(kakaoOAuthConfig.getTokenRequestUri())
                .body(params)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8") //요청 헤더
                .retrieve()
                .body(Map.class);

        assert response != null;
        return (String) response.get("access_token");
    }
}
