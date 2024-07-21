package org.layer.domain.auth.controller.dto;

import org.layer.domain.auth.service.dto.SignUpServiceResponse;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;

public record SignUpResponse(Long memberId,
                                    String name,
                                    String email,
                                    MemberRole memberRole,
                                    String SocialId,
                                    SocialType socialType,
                                    String accessToken) {
    public static SignUpResponse of(SignUpServiceResponse signUpServiceResponse) {
        return new SignUpResponse(signUpServiceResponse.memberId(),
                signUpServiceResponse.name(),
                signUpServiceResponse.email(),
                signUpServiceResponse.memberRole(),
                signUpServiceResponse.socialId(),
                signUpServiceResponse.socialType(),
                signUpServiceResponse.accessToken());
    }
}
