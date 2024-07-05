package org.layer.auth.jwt;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;

import lombok.RequiredArgsConstructor;
import org.layer.config.AuthValueConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

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
