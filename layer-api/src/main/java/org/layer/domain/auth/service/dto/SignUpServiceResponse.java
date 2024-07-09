package org.layer.domain.auth.service.dto;

import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;

public record SignUpServiceResponse(Long memberId,
                                    String name,
                                    String email,
                                    MemberRole memberRole,
                                    String socialId,
                                    SocialType socialType) {
    public static SignUpServiceResponse of(Member member) {
        return new SignUpServiceResponse(member.getId(),
                member.getName(),
                member.getEmail(),
                member.getMemberRole(),
                member.getSocialId(),
                member.getSocialType());
    }
}
