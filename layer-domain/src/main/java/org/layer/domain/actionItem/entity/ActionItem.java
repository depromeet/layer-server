package org.layer.domain.actionItem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.actionItem.enums.ActionItemStatus;
import org.layer.domain.actionItem.exception.ActionItemException;
import org.layer.domain.common.BaseTimeEntity;

import static org.layer.common.exception.ActionItemExceptionType.CANNOT_DELETE_ACTION_ITEM;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ActionItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long retrospectId; // 어떤 회고에 매핑되는지

    @NotNull
    private Long spaceId; // 어떤 스페이스에 속하는지(반정규화)

    @NotNull
    private Long memberId; // 작성자

    @NotNull
    private String content; // 액션 아이템 내용

    @NotNull
    @Enumerated(EnumType.STRING)
    private ActionItemStatus actionItemStatus; // 액션 아이템 상태

    @NotNull
    Boolean isPinned; // 핀 돼있는지

    @Builder
    private ActionItem(Long retrospectId, Long spaceId, Long memberId, String content, ActionItemStatus actionItemStatus, Boolean isPinned) {
        this.retrospectId = retrospectId;
        this.spaceId = spaceId;
        this.memberId = memberId;
        this.content = content;
        this.actionItemStatus = actionItemStatus;
        this.isPinned = isPinned;
    }

    public void isWriter(Long memberId) {
        // 액션 아이템을 작성한 사람이 아님
        if(!this.getMemberId().equals(memberId)) {
            throw new ActionItemException(CANNOT_DELETE_ACTION_ITEM);
        }
    }
}