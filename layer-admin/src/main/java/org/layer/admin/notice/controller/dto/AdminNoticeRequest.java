package org.layer.admin.notice.controller.dto;

import java.time.LocalDateTime;

import org.layer.domain.notice.entity.Notice;
import org.layer.domain.notice.entity.NoticeCategory;

import jakarta.validation.constraints.NotNull;

public class AdminNoticeRequest {

    public record CreateNoticeRequest(
        @NotNull
        String title,
        @NotNull
        String content,
        @NotNull
        NoticeCategory category,
        @NotNull
        Boolean isPinned,
        @NotNull
        Boolean isActive,
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
        public Notice toEntity() {
            return Notice.builder()
                .title(title)
                .content(content)
                .category(category)
                .isPinned(isPinned)
                .isActive(isActive)
                .startAt(startAt)
                .endAt(endAt)
                .build();
        }
    }

    public record UpdateNoticeRequest(
        @NotNull
        String title,
        @NotNull
        String content,
        @NotNull
        NoticeCategory category,
        @NotNull
        Boolean isPinned,
        @NotNull
        Boolean isActive,
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
    }
}
