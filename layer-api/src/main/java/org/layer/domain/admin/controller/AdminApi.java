package org.layer.domain.admin.controller;

import java.time.LocalDateTime;

import org.layer.domain.admin.controller.dto.AdminRetrospectsGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpacesGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "어드민", description = "어드민 관련 API")
public interface AdminApi {
	@Operation(summary = "스페이스 관련 데이터 조회", description = "")
	@Parameters({
		@Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true),
		@Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true),
		@Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true)
	})
	ResponseEntity<AdminSpacesGetResponse> getSpaceData(@RequestParam("startDate") LocalDateTime startDate,
		@RequestParam("endDate") LocalDateTime endDate, @RequestParam("password") String password);

	@Operation(summary = "회고 관련 데이터 조회", description = "")
	@Parameters({
		@Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true, schema = @Schema(type = "string")),
		@Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true, schema = @Schema(type = "string")),
		@Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true, schema = @Schema(type = "string", format = "string"))})
	ResponseEntity<AdminRetrospectsGetResponse> getRetrospectData(@RequestParam("startDate") LocalDateTime startDate,
		@RequestParam("endDate") LocalDateTime endDate, @RequestParam("password") String password);
}
