package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record SpaceActionItemResponse(@NotNull
                                     @Schema(description = "액션 아이템이 속한 스페이스 ID")
                                     Long spaceId,
                                      @NotNull
                                     @Schema(description = "액션 아이템이 속한 스페이스 이름")
                                     String spaceName,
                                      @NotNull
                                     @Schema(description = "스페이스의 액션아이템 리스트")
                                     List<SpaceActionItemElementResponse> teamActionItemList
                                     ) {
}
