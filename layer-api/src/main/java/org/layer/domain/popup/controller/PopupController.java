package org.layer.domain.popup.controller;

import org.layer.domain.popup.controller.dto.PopupResponse.PopupElement;
import org.layer.domain.popup.service.PopupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popup")
public class PopupController {

    private final PopupService popupService;

    // 활성 팝업이 없으면 body가 null인 200 응답을 반환한다.
    @GetMapping
    public ResponseEntity<PopupElement> getActivePopup() {
        return ResponseEntity.ok(popupService.getActivePopup());
    }
}
