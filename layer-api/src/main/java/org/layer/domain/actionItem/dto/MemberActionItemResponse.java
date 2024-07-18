package org.layer.domain.actionItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.actionItem.entity.ActionItem;

@Builder
@Schema(description = "특정 회원이 작성한 액션 아이템")
public record MemberActionItemResponse (@NotNull
                                        @Schema(description = "액션아이템 ID")
                                        Long actionItemId,
                                        @NotNull
                                        @Schema(description = "액션아이템 내용")
                                        String actionItemContent,
                                        @NotNull
                                        @Schema(description = "액션 아이템이 속해 있는 스페이스 ID")
                                        Long spaceId,
                                        @NotNull
                                        @Schema(description = "액션 아이템이 속해 있는 스페이스 이름")
                                        String spaceName,
                                        @NotNull
                                        @Schema(description = "액션 아이템과 매핑되는 회고 ID")
                                        Long retrospectId,
                                        @NotNull
                                        @Schema(description = "액션 아이템과 매핑되는 회고 이름")
                                        String retrospectName,
                                        @NotNull
                                        @Schema(description = "팀 스페이스의 액션 아이템인지, 개인 스페이스의 액션 아이템인지")
                                        Boolean isTeam){

    public static MemberActionItemResponse toResponse(ActionItem actionItem, String spaceName, String retrospectName, boolean isTeam) {
        return MemberActionItemResponse.builder()
                .actionItemId(actionItem.getId())
                .actionItemContent(actionItem.getContent())
                .spaceId(actionItem.getSpaceId())
                .spaceName(spaceName)
                .retrospectId(actionItem.getRetrospectId())
                .retrospectName(retrospectName)
                .isTeam(isTeam)
                .build();
    }
}
