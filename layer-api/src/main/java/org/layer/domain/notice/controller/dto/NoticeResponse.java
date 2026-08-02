package org.layer.domain.notice.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.notice.entity.Notice;
import org.layer.domain.notice.entity.NoticeCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class NoticeResponse {

    @Builder
    @Schema(description = "공지사항 요약 정보")
    public record NoticeSummary(
        @Schema(description = "공지사항 아이디")
        Long id,
        @Schema(description = "제목")
        String title,
        @Schema(description = "카테고리")
        NoticeCategory category,
        @Schema(description = "상단 고정 여부")
        Boolean isPinned,
        @Schema(description = "노출 시작 일시")
        LocalDateTime startAt,
        @Schema(description = "노출 종료 일시")
        LocalDateTime endAt,
        @Schema(description = "작성 일시")
        LocalDateTime createdAt
    ) {
        public static NoticeSummary of(Notice notice) {
            return NoticeSummary.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .category(notice.getCategory())
                .isPinned(notice.getIsPinned())
                .startAt(notice.getStartAt())
                .endAt(notice.getEndAt())
                .createdAt(notice.getCreatedAt())
                .build();
        }
    }

    @Builder
    @Schema
    public record NoticeListResponse(
        List<NoticeSummary> notices
    ) {
        public static NoticeListResponse of(List<Notice> notices) {
            return NoticeListResponse.builder()
                .notices(notices.stream().map(NoticeSummary::of).toList())
                .build();
        }
    }

    @Builder
    @Schema(description = "공지사항 상세 정보")
    public record NoticeDetail(
        @Schema(description = "공지사항 아이디")
        Long id,
        @Schema(description = "제목")
        String title,
        @Schema(description = "본문")
        String content,
        @Schema(description = "카테고리")
        NoticeCategory category,
        @Schema(description = "상단 고정 여부")
        Boolean isPinned,
        @Schema(description = "노출 시작 일시")
        LocalDateTime startAt,
        @Schema(description = "노출 종료 일시")
        LocalDateTime endAt,
        @Schema(description = "작성 일시")
        LocalDateTime createdAt
    ) {
        public static NoticeDetail of(Notice notice) {
            return NoticeDetail.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .isPinned(notice.getIsPinned())
                .startAt(notice.getStartAt())
                .endAt(notice.getEndAt())
                .createdAt(notice.getCreatedAt())
                .build();
        }
    }
}
