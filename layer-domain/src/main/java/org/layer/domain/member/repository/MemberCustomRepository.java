package org.layer.domain.member.repository;

import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;

import java.util.Optional;

public interface MemberCustomRepository {
    Optional<Member> findValidMember(String socialId, SocialType socialType);
}
