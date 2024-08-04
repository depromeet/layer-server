package org.layer.domain.actionItem.repository;

import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.enums.ActionItemStatus;
import org.layer.domain.actionItem.exception.ActionItemException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.layer.common.exception.ActionItemExceptionType.NOT_FOUND_ACTION_ITEM;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
    List<ActionItem> findAllByMemberIdAndActionItemStatusOrderByCreatedAtDesc(Long memberId, ActionItemStatus actionItemStatus);

    List<ActionItem> findAllBySpaceIdAndActionItemStatusOrderByCreatedAtDesc(Long spaceId, ActionItemStatus actionItemStatus);

    default ActionItem findByIdOrThrow(Long actionItemId){
        return findById(actionItemId)
                .orElseThrow(() -> new ActionItemException(NOT_FOUND_ACTION_ITEM));
    }

    List<ActionItem> findAllByRetrospectId(Long retrospectId);
    List<ActionItem> findAllByRetrospectIdIn(List<Long> retrospectId);
}
