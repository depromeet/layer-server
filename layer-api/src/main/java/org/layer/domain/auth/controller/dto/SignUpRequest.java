package org.layer.domain.auth.controller.dto;

import org.layer.domain.member.entity.SocialType;

public record SignUpRequest(SocialType socialType, String name) {
}
