package org.layer.domain.space.repository;

import org.layer.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceCustomRepository {
}
