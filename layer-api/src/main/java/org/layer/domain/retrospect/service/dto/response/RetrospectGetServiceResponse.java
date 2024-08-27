package org.layer.domain.retrospect.service.dto.response;

import org.layer.domain.retrospect.entity.RetrospectStatus;

import java.time.LocalDateTime;

public record RetrospectGetServiceResponse(
        Long retrospectId,
        String title,
        String introduction,
        boolean isWrite,
        RetrospectStatus retrospectStatus,
        long writeCount,
        long totalCount,

        LocalDateTime createdAt,

        LocalDateTime deadline
) {
    public static RetrospectGetServiceResponse of(Long retrospectId, String title, String introduction, boolean isWrite,
                                                  RetrospectStatus retrospectStatus, long writeCount, long totalCount, LocalDateTime createdAt, LocalDateTime deadline) {
        return new RetrospectGetServiceResponse(retrospectId, title, introduction, isWrite, retrospectStatus,
                writeCount, totalCount, createdAt, deadline);
    }
}
