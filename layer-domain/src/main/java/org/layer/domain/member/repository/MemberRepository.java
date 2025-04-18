package org.layer.domain.member.repository;

import static org.layer.global.exception.MemberExceptionType.*;

import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_USER));
    }

    List<Member> findAllByIdIn(List<Long> memberIds);
}
