package org.layer.domain.actionItem.repository;

import org.layer.domain.actionItem.entity.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
}
