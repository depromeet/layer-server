package org.layer.domain.retrospect.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.entity.WriteStatus;

import java.time.LocalDateTime;

@Schema(name = "RetrospectGetResponse", description = "특정 회고 조회 Dto")
public record RetrospectGetResponse(
	@Schema(description = "스페이스 id", example = "1")
	Long spaceId,
	@Schema(description = "회고 id", example = "1")
	Long retrospectId,
	@Schema(description = "회고 이름", example = "중간 발표 이후")
	String title,
	@Schema(description = "회고 설명", example = "중간 발표 관련해서 KPT 회고를 해봅시다.")
	String introduction,
	@Schema(description = "회고 작성 상태", example = "NOT_STARTED")
	WriteStatus writeStatus,
	@Schema(description = "회고 상태 : PROCEEDING 나 DONE 중에 하나입니다.", example = "PROCEEDING")
	RetrospectStatus retrospectStatus,
	@Schema(description = "회고 작성 상태", example = "NOT_STARTED")
	AnalysisStatus analysisStatus,
	@Schema(description = "해당 회고 응답 수", example = "4")
	long writeCount,
	@Schema(description = "전체 인원", example = "10")
	long totalCount,
	@Schema(description = "회고 생성 일자")
	LocalDateTime createdAt,
	@Schema(description = "회고 종료 일자")
	LocalDateTime deadline
) {
	public static RetrospectGetResponse of(Long spaceId, Long retrospectId, String title, String introduction,
		WriteStatus writeStatus, RetrospectStatus retrospectStatus, AnalysisStatus analysisStatus,
		long writeCount, long totalCount, LocalDateTime createdAt, LocalDateTime deadline) {

		return new RetrospectGetResponse(spaceId, retrospectId, title, introduction, writeStatus, retrospectStatus,
			analysisStatus, writeCount, totalCount, createdAt, deadline);
	}
}
