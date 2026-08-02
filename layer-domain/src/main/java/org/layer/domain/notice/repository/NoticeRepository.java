package org.layer.domain.notice.repository;

import static org.layer.global.exception.NoticeExceptionType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.notice.entity.Notice;
import org.layer.domain.notice.exception.NoticeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    default Notice findByIdOrThrow(Long noticeId) {
        return findById(noticeId)
            .orElseThrow(() -> new NoticeException(NOT_FOUND_NOTICE));
    }

    @Query("""
        SELECT n FROM Notice n
        WHERE n.isActive = true
        AND (n.startAt IS NULL OR n.startAt <= :now)
        AND (n.endAt IS NULL OR n.endAt >= :now)
        ORDER BY n.isPinned DESC, n.createdAt DESC
    """)
    List<Notice> findAllExposedNotices(@Param("now") LocalDateTime now);
}
