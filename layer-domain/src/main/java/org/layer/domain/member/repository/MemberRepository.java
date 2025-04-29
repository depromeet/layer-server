package org.layer.domain.member.repository;

import org.layer.domain.member.entity.Member;
import org.layer.domain.member.exception.MemberException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.layer.global.exception.MemberExceptionType.NOT_FOUND_USER;

public interface MemberRepository extends MemberCustomRepository, JpaRepository<Member, Long> {
    List<Member> findAllByIdIn(List<Long> memberIds);

    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_USER));
    }
}
