package org.layer.domain.actionItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TeamActionItemRequest(
                                    @NotNull
                                    @Schema(description = "액션 아이템 ID")
                                    Long actionItemId,
                                    @NotNull
                                    @Schema(description = "액션 아이템과 내용")
                                    String actionItemContent,
                                    @NotNull
                                    @Schema(description = "액션 아이템이 속한 스페이스 ID")
                                    Long spaceId,
                                    @NotNull
                                    @Schema(description = "액션 아이템이 속한 스페이스 이름")
                                    String spaceName,
                                    @NotNull
                                    @Schema(description = "액션 아이템과 매핑되는 회고 ID")
                                    Long retrospectId,
                                    @NotNull
                                    @Schema(description = "액션 아이템과 매핑되는 회고 이름")
                                    String retrospectName) {
}
