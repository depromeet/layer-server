package org.layer.domain.member.repository;

import org.layer.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends MemberCustomRepository, JpaRepository<Member, Long> {
    List<Member> findAllByIdIn(List<Long> memberIds);

}