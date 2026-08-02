package org.layer.admin.notice.controller;

import org.layer.admin.notice.controller.dto.AdminNoticeRequest.CreateNoticeRequest;
import org.layer.admin.notice.controller.dto.AdminNoticeRequest.UpdateNoticeRequest;
import org.layer.admin.notice.controller.dto.AdminNoticeResponse.NoticeCreateResponse;
import org.layer.admin.notice.controller.dto.AdminNoticeResponse.NoticeDetail;
import org.layer.admin.notice.controller.dto.AdminNoticeResponse.NoticePage;
import org.layer.admin.notice.service.AdminNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @GetMapping("")
    public ResponseEntity<NoticePage> getNotices(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminNoticeService.getNotices(page, size));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetail> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(adminNoticeService.getNotice(noticeId));
    }

    @PostMapping("")
    public ResponseEntity<NoticeCreateResponse> createNotice(@RequestBody @Valid CreateNoticeRequest request) {
        Long noticeId = adminNoticeService.createNotice(request);
        return ResponseEntity.ok(NoticeCreateResponse.builder().noticeId(noticeId).build());
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(@PathVariable Long noticeId,
        @RequestBody @Valid UpdateNoticeRequest request) {
        adminNoticeService.updateNotice(noticeId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        adminNoticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
