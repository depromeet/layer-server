package org.layer.admin.popup.controller;

import org.layer.admin.popup.controller.dto.AdminPopupRequest.CreatePopupRequest;
import org.layer.admin.popup.controller.dto.AdminPopupRequest.UpdatePopupActiveRequest;
import org.layer.admin.popup.controller.dto.AdminPopupRequest.UpdatePopupRequest;
import org.layer.admin.popup.controller.dto.AdminPopupResponse.PopupCreateResponse;
import org.layer.admin.popup.controller.dto.AdminPopupResponse.PopupDetail;
import org.layer.admin.popup.controller.dto.AdminPopupResponse.PopupPage;
import org.layer.admin.popup.service.AdminPopupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/popup")
public class AdminPopupController {

    private final AdminPopupService adminPopupService;

    @GetMapping("")
    public ResponseEntity<PopupPage> getPopups(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminPopupService.getPopups(page, size));
    }

    @GetMapping("/{popupId}")
    public ResponseEntity<PopupDetail> getPopup(@PathVariable Long popupId) {
        return ResponseEntity.ok(adminPopupService.getPopup(popupId));
    }

    @PostMapping("")
    public ResponseEntity<PopupCreateResponse> createPopup(@RequestBody @Valid CreatePopupRequest request) {
        Long popupId = adminPopupService.createPopup(request);
        return ResponseEntity.ok(PopupCreateResponse.builder().popupId(popupId).build());
    }

    @PutMapping("/{popupId}")
    public ResponseEntity<Void> updatePopup(@PathVariable Long popupId,
        @RequestBody @Valid UpdatePopupRequest request) {
        adminPopupService.updatePopup(popupId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{popupId}/active")
    public ResponseEntity<Void> updatePopupActive(@PathVariable Long popupId,
        @RequestBody @Valid UpdatePopupActiveRequest request) {
        adminPopupService.updatePopupActive(popupId, request);
        return ResponseEntity.ok().build();
    }
}
