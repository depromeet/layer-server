package org.layer.oauth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class GoogleOAuthConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String GOOGLE_CODE_URI = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String GOOGLE_TOKEN_URI = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_USER_INFO_URI = "https://www.googleapis.com/userinfo/v2/me";


    @Value("${google.login.client_id}")
    private String googleClientId;

    @Value("${google.login.client_secret}")
    private String googleClientSecret;

    @Value("${google.login.redirect_uri}")
    private String googleRedirectUri;
}
