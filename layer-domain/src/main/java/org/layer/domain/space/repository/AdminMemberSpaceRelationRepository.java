package org.layer.domain.space.repository;

import org.layer.domain.space.entity.MemberSpaceRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberSpaceRelationRepository extends JpaRepository<MemberSpaceRelation, Long> {

	Long countAllByMemberId(Long memberId);
}
