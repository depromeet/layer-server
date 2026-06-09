package org.layer.domain.actionItem.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.space.entity.Space;

import java.util.List;

@Builder
public record PersonalSpaceRetrospectActionItemGetResponse(
        @NotNull
        @Schema(description = "스페이스 ID")
        Long spaceId,

        @NotNull
        @Schema(description = "스페이스 이름")
        String spaceName,

        @NotNull
        @Schema(description = "회고별 개인 실행목표 리스트")
        List<RetrospectActionItemResponse> personalActionItemList
) {
    public static PersonalSpaceRetrospectActionItemGetResponse of(Space space, List<RetrospectActionItemResponse> list) {
        return PersonalSpaceRetrospectActionItemGetResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .personalActionItemList(list)
                .build();
    }
}
