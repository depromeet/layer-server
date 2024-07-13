package org.layer.domain.space.repository;

import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SpaceRepository extends JpaRepository<Space, Long> {
    @Query("SELECT s,COUNT(msr.id) FROM Space s JOIN MemberSpaceRelation msr ON s.id = msr.spaceId WHERE msr.memberId = :memberId AND (:cursorId IS NULL OR msr.id > :cursorId) AND s.category = :category GROUP BY s.id ORDER BY msr.id ASC")
    Page<SpaceWithMemberCount> findAllSpacesByMemberIdAndCategoryAndCursor(Long memberId, Long cursorId, SpaceCategory category, Pageable pageable);

    @Query("SELECT s,COUNT(msr.id) FROM Space s JOIN MemberSpaceRelation msr ON s.id = msr.spaceId WHERE msr.memberId = :memberId AND (:cursorId IS NULL OR msr.id > :cursorId) GROUP BY s.id ORDER BY msr.id ASC")
    Page<SpaceWithMemberCount> findAllSpacesByMemberIdAndCursor(Long memberId, Long cursorId, Pageable pageable);
}
