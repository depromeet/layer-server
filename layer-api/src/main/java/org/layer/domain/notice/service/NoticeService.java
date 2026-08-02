package org.layer.domain.notice.service;

import org.layer.domain.common.time.Time;
import org.layer.domain.notice.controller.dto.NoticeResponse.NoticeDetail;
import org.layer.domain.notice.controller.dto.NoticeResponse.NoticeListResponse;
import org.layer.domain.notice.entity.Notice;
import org.layer.domain.notice.exception.NoticeException;
import org.layer.domain.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import static org.layer.global.exception.NoticeExceptionType.NOT_FOUND_NOTICE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final Time time;

    public NoticeListResponse getExposedNotices() {
        return NoticeListResponse.of(noticeRepository.findAllExposedNotices(time.now()));
    }

    public NoticeDetail getExposedNotice(Long noticeId) {
        Notice notice = noticeRepository.findByIdOrThrow(noticeId);
        if (!notice.isExposedNow(time.now())) {
            throw new NoticeException(NOT_FOUND_NOTICE);
        }
        return NoticeDetail.of(notice);
    }
}
