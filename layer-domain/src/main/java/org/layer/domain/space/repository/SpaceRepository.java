package org.layer.domain.space.repository;

import static org.layer.global.exception.SpaceExceptionType.*;

import java.util.List;

import org.layer.domain.space.dto.SpaceWithRetrospectCount;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.SpaceException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceCustomRepository {
    default Space findByIdOrThrow(Long spaceId) {
        return findById(spaceId)
                .orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
    }

    @Query("""
        SELECT new org.layer.domain.space.dto.SpaceWithRetrospectCount(
            s.id,
            COUNT(r.id),
            COUNT(CASE WHEN r.status = 'PROCEEDING' THEN 1 END)
        )
        FROM Space s
        LEFT JOIN s.retrospectList r
        WHERE s.id IN :spaceIds
        GROUP BY s.id
    """)
    List<SpaceWithRetrospectCount> findAllSpaceWithRetrospectCount(List<Long> spaceIds);
}
