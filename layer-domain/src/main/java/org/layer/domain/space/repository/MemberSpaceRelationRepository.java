package org.layer.domain.space.repository;


import org.layer.domain.retrospect.dto.SpaceMemberCount;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface MemberSpaceRelationRepository extends JpaRepository<MemberSpaceRelation, Long> {

    Optional<MemberSpaceRelation> findBySpaceIdAndMemberId(Long spaceId, Long memberId);

    List<MemberSpaceRelation> findAllBySpaceId(Long spaceId);

    @Query("SELECT m FROM MemberSpaceRelation m WHERE m.space.id IN :spaceIds")
    List<MemberSpaceRelation> findAllBySpaceIdIn(@Param("spaceIds") List<Long> spaceIds);

    @Query("SELECT new org.layer.domain.retrospect.dto.SpaceMemberCount(m.space.id, COUNT(m)) " +
            "FROM MemberSpaceRelation m " +
            "WHERE m.space.id IN :spaceIds " +
            "GROUP BY m.space.id")
    List<SpaceMemberCount> countMembersBySpaceIds(@Param("spaceIds") List<Long> spaceIds);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MemberSpaceRelation m where m.space.id = :spaceId")
    void deleteAllBySpaceIdInBatch(Long spaceId);

    @Query("SELECT m FROM MemberSpaceRelation m JOIN FETCH m.space WHERE m.memberId = :memberId")
    List<MemberSpaceRelation> findAllByMemberId(@Param("memberId") Long memberId);

    Long countAllBySpace(Space space);

    Boolean existsByMemberIdAndSpace(Long memberId, Space space);
}
