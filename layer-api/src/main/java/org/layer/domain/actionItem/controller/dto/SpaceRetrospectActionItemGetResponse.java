package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.space.entity.Space;

import java.util.List;

@Builder
public record SpaceRetrospectActionItemGetResponse(@NotNull
                                      @Schema(description = "액션 아이템이 속한 스페이스 ID")
                                      Long spaceId,
                                                   @NotNull
                                      @Schema(description = "액션 아이템이 속한 스페이스 이름")
                                      String spaceName,
                                                   @NotNull
                                      @Schema(description = "스페이스의 액션아이템 리스트")
                                      List<RetrospectActionItemResponse> teamActionItemList
) {

    public static SpaceRetrospectActionItemGetResponse of(Space space, List<RetrospectActionItemResponse> actionItemList) {
        return SpaceRetrospectActionItemGetResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .teamActionItemList(actionItemList)
                .build();
    }
}
