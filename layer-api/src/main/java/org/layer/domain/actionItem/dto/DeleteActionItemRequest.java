package org.layer.domain.actionItem.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteActionItemRequest(@NotNull Long actionItemId) {
}
