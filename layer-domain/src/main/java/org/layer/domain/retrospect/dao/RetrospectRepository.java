package org.layer.domain.retrospect.dao;

import org.layer.domain.retrospect.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

}
