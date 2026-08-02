package org.layer.admin.notice.service;

import org.layer.admin.notice.controller.dto.AdminNoticeRequest.CreateNoticeRequest;
import org.layer.admin.notice.controller.dto.AdminNoticeRequest.UpdateNoticeRequest;
import org.layer.admin.notice.controller.dto.AdminNoticeResponse.NoticeDetail;
import org.layer.admin.notice.controller.dto.AdminNoticeResponse.NoticePage;
import org.layer.domain.notice.entity.Notice;
import org.layer.domain.notice.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminNoticeService {

    private final NoticeRepository noticeRepository;

    public NoticePage getNotices(int page, int size) {
        Page<Notice> notices = noticeRepository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return NoticePage.of(notices);
    }

    public NoticeDetail getNotice(Long noticeId) {
        Notice notice = noticeRepository.findByIdOrThrow(noticeId);
        return NoticeDetail.of(notice);
    }

    @Transactional
    public Long createNotice(CreateNoticeRequest request) {
        Notice notice = noticeRepository.save(request.toEntity());
        return notice.getId();
    }

    @Transactional
    public void updateNotice(Long noticeId, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findByIdOrThrow(noticeId);
        notice.update(request.title(), request.content(), request.category(), request.isPinned(),
            request.isActive(), request.startAt(), request.endAt());
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findByIdOrThrow(noticeId);
        noticeRepository.delete(notice);
    }
}
