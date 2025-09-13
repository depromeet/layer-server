package org.layer.domain.retrospect.repository;

import static org.layer.global.exception.RetrospectExceptionType.*;

import org.layer.domain.actionItem.dto.MemberActionItemResponse;
import org.layer.domain.retrospect.dto.SpaceRetrospectDto;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.exception.RetrospectException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
    List<Retrospect> findAllBySpaceId(Long spaceId);

	List<Retrospect> findAllBySpaceIdIn(List<Long> spaceIds);

    List<Retrospect> findAllByDeadlineBeforeAndRetrospectStatus(LocalDateTime now, RetrospectStatus retrospectStatus);

    default Retrospect findByIdOrThrow(Long retrospectId) {
        return findById(retrospectId)
                .orElseThrow(() -> new RetrospectException(NOT_FOUND_RETROSPECT));
    }

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Retrospect r WHERE r.spaceId = :spaceId")
    void deleteAllBySpaceId(Long spaceId);

    @Query("SELECT DISTINCT new org.layer.domain.actionItem.dto.MemberActionItemResponse(s, r) " +
            "FROM Retrospect r " +
            "JOIN Space s ON r.spaceId = s.id " +
            "JOIN MemberSpaceRelation ms ON ms.space.id = s.id " +
            "WHERE ms.memberId = :memberId AND r.retrospectStatus = 'DONE' " +
            "ORDER BY r.deadline DESC")
    List<MemberActionItemResponse> findAllMemberActionItemResponsesByMemberId(@Param("memberId") Long memberId);

	@Query("SELECT new org.layer.domain.retrospect.dto.SpaceRetrospectDto(s, r) " +
		"FROM Retrospect r " +
		"JOIN Space s ON r.spaceId = s.id " +
		"WHERE r.spaceId = :spaceId " +
		"AND r.retrospectStatus = :retrospectStatus " +
		"AND r.deadline > :twoMonthsAgo " +
		"ORDER BY r.deadline ASC " +
		"LIMIT 1")
	Optional<SpaceRetrospectDto> findFirstBySpaceIdAndRetrospectStatusAndDeadlineAfterOrderByDeadline(Long spaceId,
		RetrospectStatus retrospectStatus, LocalDateTime twoMonthsAgo);

}
