package org.layer.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Configuration
public class AuthValueConfig {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30L; // 30분
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14L; // 2주


    @PostConstruct
    protected void init() {
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
