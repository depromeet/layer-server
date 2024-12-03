package org.layer.domain.member.repository;

import org.layer.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<Member, Long> {
	Page<Member> findAll(Pageable pageable);
}
