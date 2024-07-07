package org.layer.oauth.dto.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRequestDto(@JsonProperty("access_token") String accessToken) {
}
