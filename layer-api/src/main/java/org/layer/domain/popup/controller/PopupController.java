package org.layer.domain.popup.controller;

import org.layer.domain.popup.controller.dto.PopupResponse.PopupListResponse;
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

    @GetMapping("/list")
    public ResponseEntity<PopupListResponse> getPopups() {
        return ResponseEntity.ok(popupService.getExposedPopups());
    }
}
