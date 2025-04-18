package org.layer.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.dto.service.google.GoogleGetMemberInfoServiceResponse;
import org.layer.oauth.exception.OAuthException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.layer.common.exception.MemberExceptionType.FAIL_TO_AUTH;
import static org.layer.domain.member.entity.SocialType.GOOGLE;
import static org.layer.oauth.config.GoogleOAuthConfig.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleService implements OAuthService {
    private final RestClient restClient;

    //== 액세스 토큰으로 사용자 정보 가져오기 ==//
    public MemberInfoServiceResponse getMemberInfo(final String accessToken) {
        GoogleGetMemberInfoServiceResponse response = null;
        try {
            response = restClient.get()
                    .uri(GOOGLE_USER_INFO_URI)
                    .header(AUTHORIZATION, TOKEN_PREFIX + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (googleRequest, googleResponse) -> {
                                throw new OAuthException(FAIL_TO_AUTH);
                            })
                    .body(GoogleGetMemberInfoServiceResponse.class);
        } catch(Exception e) {
            throw new OAuthException(FAIL_TO_AUTH);
        }

        assert response != null;
        return new MemberInfoServiceResponse(response.id(), GOOGLE, response.email());
    }
}
