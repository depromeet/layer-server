package org.layer.domain.actionItem.repository;

import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.enums.ActionItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
    List<ActionItem> findAllByMemberIdAndActionItemStatusOrderByCreatedAtDesc(Long memberId, ActionItemStatus actionItemStatus);
}
