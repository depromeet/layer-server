package org.layer.domain.member.repository;

import jakarta.validation.constraints.NotNull;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialIdAndSocialType(@NotNull String socialId, @NotNull SocialType socialType);
}
