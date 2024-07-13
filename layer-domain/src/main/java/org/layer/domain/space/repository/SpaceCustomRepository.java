package org.layer.domain.space.repository;

import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

import java.util.List;
import java.util.Optional;

public interface SpaceCustomRepository {

    /**
     * @param memberId 접속한 사용자 아이디
     * @param cursorId 커서 아이디
     * @param category 없을 경우 전체 카테고리 조회
     * @return ''
     */
    List<SpaceWithMemberCount> findAllSpacesByMemberIdAndCategoryAndCursor(Long memberId, Long cursorId, Optional<SpaceCategory> category, int pageSize);

    Optional<SpaceWithMemberCount> findByIdAndJoinedMemberId(Long spaceId, Long memberId);

    public Long updateSpace(Long spaceId, SpaceCategory category, SpaceField field, String name, String introduction);

}
