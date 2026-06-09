package org.layer.domain.actionItem.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.actionItem.entity.ActionItem;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "PersonalActionItemGetResponse", description = "개인 실행 목표 조회 응답 DTO")
public record PersonalActionItemGetResponse(
        @Schema(description = "개인 실행 목표 목록")
        List<PersonalActionItemElement> actionItems
) {
    public static PersonalActionItemGetResponse from(List<ActionItem> items) {
        return new PersonalActionItemGetResponse(
                items.stream().map(PersonalActionItemElement::from).toList()
        );
    }

    @Schema(name = "PersonalActionItemElement", description = "개인 실행 목표 요소")
    public record PersonalActionItemElement(
            @Schema(description = "실행 목표 ID", example = "1")
            Long actionItemId,

            @Schema(description = "실행 목표 내용", example = "다음 회고까지 문서 정리하기")
            String content,

            @Schema(description = "순서", example = "1")
            int actionItemOrder,

            @Schema(description = "생성 시각", example = "2024-01-01T12:00:00")
            LocalDateTime createdAt
    ) {
        public static PersonalActionItemElement from(ActionItem item) {
            return new PersonalActionItemElement(
                    item.getId(),
                    item.getContent(),
                    item.getActionItemOrder(),
                    item.getCreatedAt()
            );
        }
    }
}
