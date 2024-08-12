package org.layer.domain.retrospect.repository;

import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.exception.RetrospectException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.layer.common.exception.RetrospectExceptionType.NOT_FOUND_RETROSPECT;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
    List<Retrospect> findAllBySpaceId(Long spaceId);

    List<Retrospect> findByIdIn(List<Long> ids);

    List<Retrospect> findAllBySpaceIdIn(List<Long> spaceIds);

    default Retrospect findByIdOrThrow(Long retrospectId) {
        return findById(retrospectId)
                .orElseThrow(() -> new RetrospectException(NOT_FOUND_RETROSPECT));
    }

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Retrospect r WHERE r.spaceId = :spaceId")
    void deleteAllBySpaceId(Long spaceId);

}
