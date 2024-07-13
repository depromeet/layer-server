package org.layer.domain.space.repository;

import static org.layer.common.exception.SpaceExceptionType.*;

import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.SpaceException;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceCustomRepository {
    default Space findByIdOrThrow(Long spaceId){
        return findById(spaceId)
            .orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
    }
}
