package org.layer.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.layer.config.AuthValueConfig;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import static java.util.Base64.getEncoder;

@RequiredArgsConstructor
@Component
public class SecretKeyFactory {
    private final AuthValueConfig authValueConfig;

    SecretKey createSecretKey() {
        String encodedKey = getEncoder().encodeToString(authValueConfig.getJWT_SECRET().getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

}
