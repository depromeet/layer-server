package org.layer.domain.actionItem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.actionItem.enums.ActionItemStatus;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActionItem {
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
    private ActionItemStatus actionItemStatus; // 액션 아이템 상태
}
