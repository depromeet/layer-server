package org.layer.auth.dto.controller;

import org.layer.auth.dto.service.SignUpServiceResponse;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;

public record SignUpResponse(Long memberId,
                                    String name,
                                    String email,
                                    MemberRole memberRole,
                                    String SocialId,
                                    SocialType socialType) {
    public static SignUpResponse of(SignUpServiceResponse signUpServiceResponse) {
        return new SignUpResponse(signUpServiceResponse.memberId(),
                signUpServiceResponse.name(),
                signUpServiceResponse.email(),
                signUpServiceResponse.memberRole(),
                signUpServiceResponse.socialId(),
                signUpServiceResponse.socialType());
    }
}
