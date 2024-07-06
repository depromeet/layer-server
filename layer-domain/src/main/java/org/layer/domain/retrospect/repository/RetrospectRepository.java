package org.layer.domain.retrospect.repository;

import org.layer.domain.retrospect.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

}
