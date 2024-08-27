package org.layer.domain.actionItem.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.entity.Space;

import java.util.List;

@Builder
public record SpaceActionItemGetResponse(@NotNull
                                      @Schema(description = "액션 아이템이 속한 스페이스 ID")
                                      Long spaceId,
                                         @NotNull
                                      @Schema(description = "액션 아이템이 속한 스페이스 이름")
                                      String spaceName,
                                         @NotNull
                                      @Schema(description = "스페이스의 액션아이템 리스트")
                                      List<SpaceActionItemElementResponse> teamActionItemList
                                     ) {

    public static SpaceActionItemGetResponse of(Space space, Retrospect retrospect, List<ActionItem> spaceActionItemList) {
        List<SpaceActionItemElementResponse> actionItemElements = spaceActionItemList.stream()
                .map(a -> SpaceActionItemElementResponse.of(a, retrospect))
                .toList();

        return SpaceActionItemGetResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .teamActionItemList(actionItemElements)
                .build();
    }
}
