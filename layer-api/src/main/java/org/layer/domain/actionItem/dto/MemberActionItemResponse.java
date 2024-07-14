package org.layer.domain.actionItem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberActionItemResponse (@NotNull Long actionItemId,
                                        @NotNull String actionItemContent,
                                        @NotNull Long spaceId,
                                        @NotNull String spaceName,
                                        @NotNull Long retrospectId,
                                        @NotNull String retrospectName,
                                        @NotNull Boolean isTeam){
}
