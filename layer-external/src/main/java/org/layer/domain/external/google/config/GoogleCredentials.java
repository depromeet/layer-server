package org.layer.domain.external.google.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "google.credentials")
public class GoogleCredentials {

    private Installed installed;

    @Data
    public static class Installed {
        private String clientId;
        private String projectId;
        private String authUri;
        private String tokenUri;
        private String authProviderX509CertUrl;
        private String clientSecret;
        private List<String> redirectUris;
    }
}