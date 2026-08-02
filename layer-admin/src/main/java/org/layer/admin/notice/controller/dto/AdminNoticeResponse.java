package org.layer.admin.notice.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.notice.entity.Notice;
import org.layer.domain.notice.entity.NoticeCategory;
import org.springframework.data.domain.Page;

import lombok.Builder;

public class AdminNoticeResponse {

    @Builder
    public record NoticeCreateResponse(
        Long noticeId
    ) {
    }

    @Builder
    public record NoticeSummary(
        Long id,
        String title,
        NoticeCategory category,
        Boolean isPinned,
        Boolean isActive,
        LocalDateTime startAt,
        LocalDateTime endAt,
        LocalDateTime createdAt
    ) {
        public static NoticeSummary of(Notice notice) {
            return NoticeSummary.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .category(notice.getCategory())
                .isPinned(notice.getIsPinned())
                .isActive(notice.getIsActive())
                .startAt(notice.getStartAt())
                .endAt(notice.getEndAt())
                .createdAt(notice.getCreatedAt())
                .build();
        }
    }

    @Builder
    public record NoticeDetail(
        Long id,
        String title,
        String content,
        NoticeCategory category,
        Boolean isPinned,
        Boolean isActive,
        LocalDateTime startAt,
        LocalDateTime endAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        public static NoticeDetail of(Notice notice) {
            return NoticeDetail.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .isPinned(notice.getIsPinned())
                .isActive(notice.getIsActive())
                .startAt(notice.getStartAt())
                .endAt(notice.getEndAt())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
        }
    }

    @Builder
    public record NoticePage(
        List<NoticeSummary> notices,
        long totalCount,
        int totalPages
    ) {
        public static NoticePage of(Page<Notice> page) {
            return NoticePage.builder()
                .notices(page.getContent().stream().map(NoticeSummary::of).toList())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
        }
    }
}
