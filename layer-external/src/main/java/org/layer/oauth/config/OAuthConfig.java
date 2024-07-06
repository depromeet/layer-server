package org.layer.oauth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
@Getter
@PropertySource("classpath:application-oauth.properties")
@Configuration
public class OAuthConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String KAKAO_URI = "https://kapi.kakao.com/v2/user/me";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${kakao.login.api_key}")
    private String kakaoLoginApiKey;

    @Value("${kakao.login.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.login.uri.code}")
    private String codeReqeustUri;

    @Value("${kakao.login.uri.base}")
    private String kakaoAuthBaseUri;

    @Value("${kakao.login.uri.token}")
    private String tokenRequestUri;

    @Value("${kakao.api.uri.base}")
    private String kakaoApiBaseUri;

    @Value("${kakao.api.uri.user}")
    private String kakaoApiUserInfoRequestUri;

}
