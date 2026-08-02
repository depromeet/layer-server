package org.layer.domain.notice.entity;

import java.time.LocalDateTime;

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
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NoticeCategory category;

    @NotNull
    private Boolean isPinned;

    @NotNull
    private Boolean isActive;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Builder
    public Notice(String title, String content, NoticeCategory category, Boolean isPinned, Boolean isActive,
        LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.isPinned = isPinned;
        this.isActive = isActive;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void update(String title, String content, NoticeCategory category, Boolean isPinned, Boolean isActive,
        LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.isPinned = isPinned;
        this.isActive = isActive;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public boolean isExposedNow(LocalDateTime now) {
        if (!isActive) {
            return false;
        }
        if (startAt != null && now.isBefore(startAt)) {
            return false;
        }
        if (endAt != null && now.isAfter(endAt)) {
            return false;
        }
        return true;
    }
}
