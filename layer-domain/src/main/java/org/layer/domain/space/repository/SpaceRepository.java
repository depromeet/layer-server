package org.layer.domain.space.repository;

import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SpaceRepository extends JpaRepository<MemberSpaceRelation, Long> {
    @Query("SELECT s FROM Space s JOIN MemberSpaceRelation msr ON s.id = msr.spaceId WHERE msr.memberId = :memberId AND (:cursorId IS NULL OR msr.id > :cursorId) AND s.category = :category ORDER BY msr.id ASC")
    Page<Space> findAllSpacesByMemberIdAndCategoryAndCursor(Long memberId, Long cursorId, SpaceCategory category, Pageable pageable);

    @Query("SELECT s FROM Space s JOIN MemberSpaceRelation msr ON s.id = msr.spaceId WHERE msr.memberId = :memberId AND (:cursorId IS NULL OR msr.id > :cursorId) ORDER BY msr.id ASC")
    Page<Space> findAllSpacesByMemberIdAndCursor(Long memberId, Long cursorId, Pageable pageable);
}
