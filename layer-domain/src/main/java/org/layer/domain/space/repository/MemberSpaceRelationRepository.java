package org.layer.domain.space.repository;


import org.layer.domain.space.entity.MemberSpaceRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberSpaceRelationRepository extends JpaRepository<MemberSpaceRelation, Long> {

    Optional<MemberSpaceRelation> findBySpaceIdAndMemberId(Long spaceId, Long memberId);

    List<MemberSpaceRelation> findAllBySpaceId(Long spaceId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MemberSpaceRelation m where m.space.id = :spaceId")
    void deleteAllBySpaceIdInBatch(Long spaceId);

    List<MemberSpaceRelation> findAllByMemberId(Long memberId);
}
