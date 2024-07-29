package org.layer.domain.space.repository;


import java.util.List;
import java.util.Optional;

import org.layer.domain.space.entity.MemberSpaceRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSpaceRelationRepository extends JpaRepository<MemberSpaceRelation, Long> {

	Optional<MemberSpaceRelation> findBySpaceIdAndMemberId(Long spaceId, Long memberId);

	List<MemberSpaceRelation> findAllBySpaceId(Long spaceId);
}
