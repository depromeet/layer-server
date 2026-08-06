package org.layer.domain.popup.controller.dto;

import org.layer.domain.popup.entity.Popup;
import org.layer.domain.popup.entity.PopupIconType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class PopupResponse {

    @Builder
    @Schema(description = "현재 활성화된 팝업 정보")
    public record PopupElement(
        @Schema(description = "팝업 아이디")
        Long id,
        @Schema(description = "아이콘 id", example = "default")
        PopupIconType iconType,
        @Schema(description = "제목")
        String title,
        @Schema(description = "내용")
        String content,
        @Schema(description = "이동 버튼 URL. null이면 이동 버튼 없이 확인 버튼만 노출")
        String moveButtonUrl
    ) {
        public static PopupElement of(Popup popup) {
            return PopupElement.builder()
                .id(popup.getId())
                .iconType(popup.getIconType())
                .title(popup.getTitle())
                .content(popup.getContent())
                .moveButtonUrl(popup.getMoveButtonUrl())
                .build();
        }
    }
}
