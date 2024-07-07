package org.layer.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.exception.BaseCustomException;
import org.layer.oauth.config.GoogleOAuthConfig;
import org.layer.oauth.dto.controller.TokenRequestDto;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.dto.service.google.GoogleGetMemberInfoServiceResponse;
import org.layer.oauth.dto.service.kakao.KakaoGetMemberInfoServiceResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.layer.common.exception.MemberExceptionType.FAIL_TO_AUTH;
import static org.layer.domain.member.entity.SocialType.GOOGLE;
import static org.layer.oauth.config.GoogleOAuthConfig.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleService {
    private final GoogleOAuthConfig googleOAuthConfig;

    //== 액세스 토큰으로 사용자 정보 가져오기 ==//
    public MemberInfoServiceResponse getMemberInfo(final String accessToken) {
        GoogleGetMemberInfoServiceResponse response = null;
        try {
            RestClient restClient = RestClient.create();
            response = restClient.get()
                    .uri(GOOGLE_USER_INFO_URI)
                    .header(AUTHORIZATION, TOKEN_PREFIX + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (googleRequest, googleResponse) -> {
                                throw new BaseCustomException(FAIL_TO_AUTH);
                            })
                    .body(GoogleGetMemberInfoServiceResponse.class);
        } catch(Exception e) {
            throw new BaseCustomException(FAIL_TO_AUTH);
        }

        assert response != null;
        return new MemberInfoServiceResponse(response.id(), GOOGLE, response.email());
    }
}
