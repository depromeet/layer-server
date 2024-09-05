package org.layer.domain.space.repository;

import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.SpaceException;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.layer.common.exception.SpaceExceptionType.NOT_FOUND_SPACE;


public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceCustomRepository {
    default Space findByIdOrThrow(Long spaceId) {
        return findById(spaceId)
                .orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
    }
}
