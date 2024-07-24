package org.layer.domain.retrospect.controller.dto.response;

import org.layer.domain.retrospect.entity.RetrospectStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RetrospectGetResponse", description = "특정 회고 조회 Dto")
public record RetrospectGetResponse(
	@Schema(description = "회고 id", example = "1")
	Long retrospectId,
	@Schema(description = "회고 이름", example = "중간 발표 이후")
	String title,
	@Schema(description = "회고 설명", example = "중간 발표 관련해서 KPT 회고를 해봅시다.")
	String introduction,
	@Schema(description = "회고 작성 여부", example = "false")
	boolean isWrite,
	@Schema(description = "회고 상태 : PROCEEDING 나 DONE 중에 하나입니다.", example = "PROCEEDING")
	RetrospectStatus retrospectStatus,
	@Schema(description = "해당 회고 응답 수", example = "4")
	long writeCount,
	@Schema(description = "전체 인원", example = "10")
	long totalCount
) {
	public static RetrospectGetResponse of(Long retrospectId, String title, String introduction, boolean isWrite, RetrospectStatus retrospectStatus,
		long writeCount, long totalCount){

		return new RetrospectGetResponse(retrospectId, title, introduction, isWrite, retrospectStatus, writeCount, totalCount);
	}
}
