package org.layer.domain.notice.controller;

import org.layer.domain.notice.controller.dto.NoticeResponse.NoticeDetail;
import org.layer.domain.notice.controller.dto.NoticeResponse.NoticeListResponse;
import org.layer.domain.notice.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public ResponseEntity<NoticeListResponse> getNotices() {
        return ResponseEntity.ok(noticeService.getExposedNotices());
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetail> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getExposedNotice(noticeId));
    }
}
