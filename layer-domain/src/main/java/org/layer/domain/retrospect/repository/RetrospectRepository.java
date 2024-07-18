package org.layer.domain.retrospect.repository;

import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.exception.RetrospectException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.layer.common.exception.RetrospectExceptionType.NOT_FOUND_RETROSPECT;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
	List<Retrospect> findAllBySpaceId(Long spaceId);

	List<Retrospect> findByIdIn(List<Long> ids);

	default Retrospect findByIdOrThrow(Long retrospectId){
		return findById(retrospectId)
			.orElseThrow(() -> new RetrospectException(NOT_FOUND_RETROSPECT));
	}

}
