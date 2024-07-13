package org.layer.domain.space.repository;

import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SpaceRepository extends JpaRepository<Space, Long> {
    default Space findByIdOrThrow(Long spaceId){
        return findById(spaceId)
            .orElseThrow(() -> new SpaceException(NOT_FOUND_SPACE));
    }
}
