package org.layer.domain.auth.controller.dto;

import lombok.Builder;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;


@Builder
public record MemberInfoResponse(Long memberId,
                                 String name,
                                 String email,
                                 MemberRole memberRole,
                                 String socialId,
                                 SocialType socialType) {
    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(member.getId(),
                member.getName(),
                member.getEmail(),
                member.getMemberRole(),
                member.getSocialId(),
                member.getSocialType());
    }
}
