package org.layer.domain.actionItem.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.entity.Space;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PersonalSpaceActionItemGetResponse(
        @NotNull
        @Schema(description = "스페이스 ID")
        Long spaceId,

        @NotNull
        @Schema(description = "스페이스 이름")
        String spaceName,

        @NotNull
        @Schema(description = "가장 최근 회고의 개인 실행목표 리스트")
        List<PersonalSpaceActionItemElement> personalActionItemList
) {
    public static PersonalSpaceActionItemGetResponse of(Space space, Retrospect retrospect, List<ActionItem> items) {
        List<PersonalSpaceActionItemElement> elements = items.stream()
                .map(item -> PersonalSpaceActionItemElement.of(item, retrospect))
                .toList();

        return PersonalSpaceActionItemGetResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .personalActionItemList(elements)
                .build();
    }

    public static PersonalSpaceActionItemGetResponse empty(Space space) {
        return PersonalSpaceActionItemGetResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .personalActionItemList(List.of())
                .build();
    }

    @Builder
    public record PersonalSpaceActionItemElement(
            @Schema(description = "실행목표 ID")
            Long actionItemId,

            @Schema(description = "실행목표 내용")
            String content,

            @Schema(description = "회고 ID")
            Long retrospectId,

            @Schema(description = "회고 제목")
            String retrospectTitle,

            @Schema(description = "생성 시각")
            LocalDateTime createdAt
    ) {
        public static PersonalSpaceActionItemElement of(ActionItem item, Retrospect retrospect) {
            return PersonalSpaceActionItemElement.builder()
                    .actionItemId(item.getId())
                    .content(item.getContent())
                    .retrospectId(retrospect.getId())
                    .retrospectTitle(retrospect.getTitle())
                    .createdAt(item.getCreatedAt())
                    .build();
        }
    }
}
