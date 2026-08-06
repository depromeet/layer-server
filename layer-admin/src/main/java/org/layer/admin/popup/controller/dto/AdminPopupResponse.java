package org.layer.admin.popup.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.popup.entity.Popup;
import org.layer.domain.popup.entity.PopupIconType;
import org.springframework.data.domain.Page;

import lombok.Builder;

public class AdminPopupResponse {

    @Builder
    public record PopupCreateResponse(
        Long popupId
    ) {
    }

    @Builder
    public record PopupSummary(
        Long id,
        PopupIconType iconType,
        String title,
        Boolean isActive,
        LocalDateTime createdAt
    ) {
        public static PopupSummary of(Popup popup) {
            return PopupSummary.builder()
                .id(popup.getId())
                .iconType(popup.getIconType())
                .title(popup.getTitle())
                .isActive(popup.getIsActive())
                .createdAt(popup.getCreatedAt())
                .build();
        }
    }

    @Builder
    public record PopupDetail(
        Long id,
        PopupIconType iconType,
        String title,
        String content,
        String moveButtonUrl,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        public static PopupDetail of(Popup popup) {
            return PopupDetail.builder()
                .id(popup.getId())
                .iconType(popup.getIconType())
                .title(popup.getTitle())
                .content(popup.getContent())
                .moveButtonUrl(popup.getMoveButtonUrl())
                .isActive(popup.getIsActive())
                .createdAt(popup.getCreatedAt())
                .updatedAt(popup.getUpdatedAt())
                .build();
        }
    }

    @Builder
    public record PopupPage(
        List<PopupSummary> popups,
        long totalCount,
        int totalPages
    ) {
        public static PopupPage of(Page<Popup> page) {
            return PopupPage.builder()
                .popups(page.getContent().stream().map(PopupSummary::of).toList())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
        }
    }
}
