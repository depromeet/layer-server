package org.layer.domain.actionItem.repository;

import static org.layer.global.exception.ActionItemExceptionType.*;

import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.entity.ActionItemType;
import org.layer.domain.actionItem.exception.ActionItemException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {

    default ActionItem findByIdOrThrow(Long actionItemId) {
        return findById(actionItemId)
                .orElseThrow(() -> new ActionItemException(NOT_FOUND_ACTION_ITEM));
    }

    List<ActionItem> findAllByRetrospectId(Long retrospectId);

    int countByRetrospectId(Long retrospectId);

    List<ActionItem> findAllByRetrospectIdIn(List<Long> retrospectIds);

    List<ActionItem> findAllByRetrospectIdAndType(Long retrospectId, ActionItemType type);

    List<ActionItem> findAllByRetrospectIdAndMemberIdAndType(Long retrospectId, Long memberId, ActionItemType type);

    int countByRetrospectIdAndMemberIdAndType(Long retrospectId, Long memberId, ActionItemType type);

    List<ActionItem> findAllByRetrospectIdInAndMemberIdAndType(List<Long> retrospectIds, Long memberId, ActionItemType type);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM ActionItem a WHERE a.spaceId = :spaceId")
    void deleteAllBySpaceId(Long spaceId);
}
