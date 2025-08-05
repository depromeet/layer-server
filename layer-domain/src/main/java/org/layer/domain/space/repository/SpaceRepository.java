package org.layer.domain.space.repository;

import static org.layer.global.exception.SpaceExceptionType.*;

import java.util.List;

import org.layer.domain.space.dto.SpaceWithRetrospectCount;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.SpaceException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceCustomRepository {
    default Space findByIdOrThrow(Long spaceId) {
        return findById(spaceId)
                .orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
    }

    @Query("""
        SELECT new org.layer.domain.space.dto.SpaceWithRetrospectCount(
            s.id,
            COUNT(r.id),
            SUM(CASE WHEN r.retrospectStatus = 'PROCEEDING' THEN 1 ELSE 0 END)
        )
        FROM Space s
        LEFT JOIN Retrospect r ON r.spaceId = s.id
        WHERE s.id IN :spaceIds
        GROUP BY s.id
    """)
    List<SpaceWithRetrospectCount> findAllSpaceWithRetrospectCount(@Param("spaceIds") List<Long> spaceIds);
}
