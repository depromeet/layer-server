package org.layer.oauth.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KakaoOAuthConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String KAKAO_URI = "https://kapi.kakao.com/v2/user/me";
    public static final String TOKEN_PREFIX = "Bearer ";
}
