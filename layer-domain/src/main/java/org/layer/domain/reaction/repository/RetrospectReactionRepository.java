package org.layer.domain.reaction.repository;

import org.layer.domain.reaction.entity.RetrospectReaction;
import org.layer.domain.reaction.exception.ReactionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static org.layer.global.exception.ReactionExceptionType.NOT_FOUND_RETROSPECT_REACTION;

public interface RetrospectReactionRepository extends JpaRepository<RetrospectReaction, Long> {

    default RetrospectReaction findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ReactionException(NOT_FOUND_RETROSPECT_REACTION));
    }

    boolean existsByAnswerIdAndMemberId(Long answerId, Long memberId);

    List<RetrospectReaction> findAllByAnswerIdIn(List<Long> answerIds);

    @Query(value = """
            SELECT rr.reaction_id
            FROM retrospect_reaction rr
            WHERE rr.member_id = :memberId
            GROUP BY rr.reaction_id
            ORDER BY MAX(rr.created_at) DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Long> findRecentDistinctReactionIdsByMemberId(@Param("memberId") Long memberId, @Param("limit") int limit);
}
