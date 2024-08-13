package org.layer.domain.actionItem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String content; // 실행 목표 내용

    @NotNull
    private int actionItemOrder; // 회고 내에서 실행 목표의 순서


    @Builder
    private ActionItem(Long retrospectId, Long spaceId, Long memberId, String content, Boolean isPinned) {
        this.retrospectId = retrospectId;
        this.spaceId = spaceId;
        this.memberId = memberId;
        this.content = content;
    }

    public void isWriter(Long memberId) {
        // 액션 아이템을 작성한 사람이 아님
        if(!this.getMemberId().equals(memberId)) {
            throw new ActionItemException(CANNOT_DELETE_ACTION_ITEM);
        }
    }
}