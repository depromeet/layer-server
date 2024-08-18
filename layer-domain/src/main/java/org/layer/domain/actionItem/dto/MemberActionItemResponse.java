package org.layer.domain.actionItem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.enums.ActionItemStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.entity.Space;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberActionItemResponse {
    @NotNull
    Long retrospectId;

    @NotNull
    String retrospectTitle;

    @NotNull
    Long spaceId;

    @NotNull
    String spaceName;
    @NotNull
    LocalDateTime answeredAt;
    @NotNull
    ActionItemStatus status;

    @NotNull
    List<ActionItemResponse> actionItemList;


    public MemberActionItemResponse(Space space,
                                    Retrospect retrospect,
                                    List<ActionItem> actionItemList,
                                    LocalDateTime answeredAt,
                                    ActionItemStatus status) {
        List<ActionItemResponse> actionItemResList = actionItemList.stream().map(ActionItemResponse::of).toList();

        this.retrospectId = retrospect.getId();
        this.retrospectTitle = retrospect.getTitle();
        this.spaceId = space.getId();
        this.spaceName = space.getName();
        this.actionItemList = actionItemResList;
        this.answeredAt = answeredAt;
        this.status = status;
    }

    public MemberActionItemResponse(Space space,
                                    Retrospect retrospect) {
        this.retrospectId = retrospect.getId();
        this.retrospectTitle = retrospect.getTitle();
        this.spaceId = space.getId();
        this.spaceName = space.getName();
    }

    public void updateActionItemList(List<ActionItemResponse> actionItemList) {
        this.actionItemList = actionItemList;
    }

    public void updateAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public void updateStatus(ActionItemStatus status) {
        this.status = status;
    }
}
