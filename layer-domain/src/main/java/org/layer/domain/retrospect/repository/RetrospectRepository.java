package org.layer.domain.retrospect.repository;

import java.util.List;

import org.layer.domain.retrospect.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
	List<Retrospect> findAllBySpaceId(Long spaceId);
}
