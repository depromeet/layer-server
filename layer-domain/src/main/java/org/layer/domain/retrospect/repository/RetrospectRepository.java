package org.layer.domain.retrospect.repository;

import static org.layer.common.exception.RetrospectExceptionType.*;

import java.util.List;

import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.exception.RetrospectException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
	List<Retrospect> findAllBySpaceId(Long spaceId);

	default Retrospect findByIdOrThrow(Long retrospectId){
		return findById(retrospectId)
			.orElseThrow(() -> new RetrospectException(NOT_FOUND_RETROSPECT));
	}
}
