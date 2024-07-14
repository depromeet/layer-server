package org.layer.domain.actionItem.dto;

import jakarta.validation.constraints.NotNull;

public record TeamActionItemRequest(@NotNull Long actionItemId,
                                    @NotNull String actionItemContent,
                                    @NotNull Long spaceId,
                                    @NotNull String spaceName,
                                    @NotNull Long retrospectId,
                                    @NotNull String retrospectName) {
}
