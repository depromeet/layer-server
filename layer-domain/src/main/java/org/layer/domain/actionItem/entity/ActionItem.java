package org.layer.domain.actionItem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.actionItem.enums.ActionItemStatus;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
    @Enumerated(EnumType.STRING)
    private ActionItemStatus actionItemStatus; // 액션 아이템 상태
}