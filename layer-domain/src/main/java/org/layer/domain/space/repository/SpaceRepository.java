package org.layer.domain.space.repository;

import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.SpaceException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static org.layer.common.exception.SpaceExceptionType.NOT_FOUND_SPACE;


public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceCustomRepository {
    default Space findByIdOrThrow(Long spaceId) {
        return findById(spaceId)
                .orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
    }

    List<Space> findByIdIn(List<Long> ids);

    @Query("select s from Space s inner join MemberSpaceRelation ms on s.id = ms.space.id where ms.memberId = :memberId")
    List<Space> findByMemberId(@Param("memberId") Long memberId);
}
