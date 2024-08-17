package org.layer.domain.member.repository;

import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.layer.common.exception.MemberExceptionType.NOT_FOUND_USER;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_USER));
    }
}
