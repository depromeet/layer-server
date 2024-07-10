package org.layer.domain.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.layer.domain.member.entity.SocialType;

public record SignInRequest(@JsonProperty("social_type") SocialType socialType) {
}
