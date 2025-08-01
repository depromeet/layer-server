package org.layer.domain.space.repository;

import org.layer.common.dto.CursorPageReq;
import org.layer.common.dto.CursorPageRes;
import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.SpaceCategory;

import java.util.List;
import java.util.Optional;

public interface SpaceCustomRepository {

    /**
     * @param memberId 접속한 사용자 아이디
     * @param category 없을 경우 전체 카테고리 조회
     * @param cursorPageReq 커서 페이지 요청
     * @return ''
     */
    CursorPageRes<SpaceWithMemberCount> findAllSpacesByMemberIdAndCategoryAndCursor(Long memberId, SpaceCategory category, CursorPageReq cursorPageReq);

}
