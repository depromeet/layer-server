package org.layer.oauth.config;


import org.layer.oauth.dto.service.apple.ApplePublicKeys;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "apple-public-key", url = "https://appleid.apple.com")
@Component
public interface AppleAuthClient {

    @GetMapping("/auth/keys")
    ApplePublicKeys getApplePublicKeys();
}