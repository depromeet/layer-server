package org.layer.domain.popup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.common.BaseTimeEntity;

@Getter
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Popup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PopupIconType iconType;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    // 이동 버튼 노출 여부는 이 값의 존재 여부로 판단한다 (null이면 확인 버튼만 노출)
    private String moveButtonUrl;

    @NotNull
    private Boolean isActive;

    @Builder
    public Popup(PopupIconType iconType, String title, String content, String moveButtonUrl, Boolean isActive) {
        this.iconType = iconType;
        this.title = title;
        this.content = content;
        this.moveButtonUrl = normalizeMoveButtonUrl(moveButtonUrl);
        this.isActive = isActive;
    }

    public void update(PopupIconType iconType, String title, String content, String moveButtonUrl, Boolean isActive) {
        this.iconType = iconType;
        this.title = title;
        this.content = content;
        this.moveButtonUrl = normalizeMoveButtonUrl(moveButtonUrl);
        this.isActive = isActive;
    }

    public void updateActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // 관리자가 폼에 빈 문자열을 제출해도 "주소 없음"으로 동일하게 취급되도록 null로 정규화한다.
    // 클라이언트는 moveButtonUrl != null 여부만으로 이동 버튼 노출을 판단하므로 여기서 통일해야 한다.
    private String normalizeMoveButtonUrl(String moveButtonUrl) {
        if (moveButtonUrl == null || moveButtonUrl.isBlank()) {
            return null;
        }
        return moveButtonUrl;
    }
}
