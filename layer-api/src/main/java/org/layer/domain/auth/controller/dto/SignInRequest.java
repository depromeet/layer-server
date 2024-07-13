package org.layer.domain.auth.controller.dto;

import org.layer.domain.member.entity.SocialType;

public record SignInRequest(SocialType socialType) {
}
