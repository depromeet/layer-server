package org.layer.domain.actionItem.repository;

import org.layer.domain.actionItem.dto.MemberActionItemResponse;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.enums.ActionItemStatus;
import org.layer.domain.actionItem.exception.ActionItemException;
import org.layer.domain.retrospect.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.layer.common.exception.ActionItemExceptionType.NOT_FOUND_ACTION_ITEM;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
    List<ActionItem> findAllByMemberIdAndActionItemStatusOrderByCreatedAtDesc(Long memberId, ActionItemStatus actionItemStatus);

    List<ActionItem> findAllBySpaceIdAndActionItemStatusOrderByCreatedAtDesc(Long spaceId, ActionItemStatus actionItemStatus);

    default ActionItem findByIdOrThrow(Long actionItemId) {
        return findById(actionItemId)
                .orElseThrow(() -> new ActionItemException(NOT_FOUND_ACTION_ITEM));
    }

    List<ActionItem> findAllByRetrospectId(Long retrospectId);

    List<ActionItem> findAllByRetrospectIdIn(List<Long> retrospectId);
  
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM ActionItem a WHERE a.spaceId = :spaceId")
    void deleteAllBySpaceId(Long spaceId);


    @Query("SELECT new org.layer.domain.actionItem.dto.MemberActionItemResponse(s, r) " +
            "FROM Retrospect r " +
            "JOIN Space s ON r.spaceId = s.id " +
            "JOIN ActionItem ai ON ai.retrospectId = r.id " +
            "WHERE r IN :doneRetrospects")
    List<MemberActionItemResponse> findAllMemberActionItemResponses(@Param("doneRetrospects") List<Retrospect> doneRetrospects);


}
