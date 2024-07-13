package org.layer.domain.space.repository;


import java.util.List;

import org.layer.domain.space.entity.MemberSpaceRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSpaceRelationRepository extends JpaRepository<MemberSpaceRelation, Long> {

	List<MemberSpaceRelation> findAllBySpaceId(Long spaceId);
}
