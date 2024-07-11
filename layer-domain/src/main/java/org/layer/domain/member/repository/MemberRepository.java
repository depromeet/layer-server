package org.layer.domain.member.repository;

import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // SELECT * FROM MEMBER WHERE socialId = ? AND socialType = ?;
    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);
}
