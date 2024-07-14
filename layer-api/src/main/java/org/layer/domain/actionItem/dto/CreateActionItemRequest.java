package org.layer.domain.actionItem.dto;

import jakarta.validation.constraints.NotNull;

public record CreateActionItemRequest(@NotNull Long retrospectId, @NotNull String content) {
}
