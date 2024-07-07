package org.layer.oauth.dto.service;

import org.layer.domain.member.entity.SocialType;

public record MemberInfoServiceResponse(String socialId, SocialType socialType, String email) {
}
