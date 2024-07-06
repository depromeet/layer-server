package org.layer.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.oauth.config.OAuthConfig;
import org.layer.oauth.dto.service.KakaoAccountServiceResponse;
import org.layer.oauth.dto.service.KakaoGetMemberInfoServiceResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.layer.oauth.config.OAuthConfig.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {
    private final OAuthConfig oAuthConfig;

    // TODO: getMemberInfo 리턴 타입 수정 필요
    public KakaoAccountServiceResponse getMemberInfo(final String accessToken) {
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
                                throw new RuntimeException(); // TODO: 수정 필요
                            })
                    .body(KakaoGetMemberInfoServiceResponse.class);
        } catch(Exception e) {
            throw e; // TODO: Exception 수정 필요
        }

        assert response != null;
        return response.kakao_account();
    }

    //== 이건 프론트에서..? TODO: 지우기 ==//
    public String getToken(String code) {
        // 토큰 요청 데이터
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oAuthConfig.getKakaoLoginApiKey());
        params.add("redirect_uri", oAuthConfig.getRedirectUri());
        params.add("code", code);


        Map response = RestClient.create(oAuthConfig.getKakaoAuthBaseUri())
                .post()
                .uri(oAuthConfig.getTokenRequestUri())
                .body(params)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8") //요청 헤더
                .retrieve()
                .body(Map.class);

        assert response != null;
        return (String) response.get("access_token");
    }
}
