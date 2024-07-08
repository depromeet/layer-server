package org.layer.oauth.dto.service.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenServiceResponse(@JsonProperty("access_token") String accessToken) {
}
