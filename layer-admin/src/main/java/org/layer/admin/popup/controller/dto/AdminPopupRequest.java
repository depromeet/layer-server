package org.layer.admin.popup.controller.dto;

import org.layer.domain.popup.entity.Popup;
import org.layer.domain.popup.entity.PopupIconType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminPopupRequest {

    public record CreatePopupRequest(
        @NotNull
        PopupIconType iconType,
        @NotNull
        @Size(max = 30)
        String title,
        @NotNull
        @Size(max = 800)
        String content,
        String moveButtonUrl,
        @NotNull
        Boolean isActive
    ) {
        public Popup toEntity() {
            return Popup.builder()
                .iconType(iconType)
                .title(title)
                .content(content)
                .moveButtonUrl(moveButtonUrl)
                .isActive(isActive)
                .build();
        }
    }

    public record UpdatePopupRequest(
        @NotNull
        PopupIconType iconType,
        @NotNull
        @Size(max = 30)
        String title,
        @NotNull
        @Size(max = 800)
        String content,
        String moveButtonUrl,
        @NotNull
        Boolean isActive
    ) {
    }

    public record UpdatePopupActiveRequest(
        @NotNull
        Boolean isActive
    ) {
    }
}
