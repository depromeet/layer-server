package org.layer.oauth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppleOAuthConfig {
    @Value("apple.login.issuer")
    private String issuer;

    @Value("${apple.login.key_id}")
    private String keyId;

    @Value("${apple.login.audience}")
    private String audience;

    @Value("${apple.login.key_id}")
    private String clientId;

    @Value("${apple.login.secret_key}")
    private String secretKey;


}
