package org.layer.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.domain.admin.controller.dto.AdminRetrospectCountGetResponse;
import org.layer.domain.admin.controller.dto.AdminRetrospectsGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpaceCountGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpacesGetResponse;
import org.layer.domain.retrospect.dto.AdminRetrospectCountGroupBySpaceGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

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

	@Operation(summary = "스페이스 개수 조회", description = "특정 기간내에 만들어진 스페이스 개수를 조회합니다. (우리 팀원이 만든 스페이스는 제외)")
	@Parameters({
			@Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true, schema = @Schema(type = "string", format = "string"))})
	ResponseEntity<AdminSpaceCountGetResponse> getSpaceCount(@RequestParam("startDate") LocalDateTime startDate,
															 @RequestParam("endDate") LocalDateTime endDate, @RequestParam("password") String password);

	@Operation(summary = "회고 개수 조회", description = "특정 기간내에 시작된 회고 개수를 조회합니다. (우리 팀원이 만든 스페이스에서 진행된 회고는 제외)")
	@Parameters({
			@Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true, schema = @Schema(type = "string", format = "string"))})
	ResponseEntity<AdminRetrospectCountGetResponse> getRetrospectCount(@RequestParam("startDate") LocalDateTime startDate,
																	   @RequestParam("endDate") LocalDateTime endDate, @RequestParam("password") String password);


	@Operation(summary = "특정 스페이스 내 회고 개수 조회", description = "특정 기간내에 특정 스페이스 안에서 시작된 회고 개수를 조회합니다.")
	@Parameters({
			@Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true, schema = @Schema(type = "string", format = "string"))})
	ResponseEntity<AdminRetrospectCountGetResponse> getRetrospectCountInSpace(@RequestParam("startDate") LocalDateTime startDate,
																			  @RequestParam("endDate") LocalDateTime endDate,
																			  @RequestParam("spaceId") Long spaceId,
																			  @RequestParam("password") String password);


	@Operation(summary = "특정 기간 내 회고 개수 스페이스 별로 보기", description = "특정 기간내에 시작된 회고 개수를 스페이스 별로 조회합니다. (우리 팀원이 만든 스페이스는 제외)")
	@Parameters({
			@Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true, schema = @Schema(type = "string")),
			@Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true, schema = @Schema(type = "string", format = "string"))})
	ResponseEntity<List<AdminRetrospectCountGroupBySpaceGetResponse>> getRetrospectCountGroupBySpace (@RequestParam("startDate") LocalDateTime startDate,
																									  @RequestParam("endDate") LocalDateTime endDate,
																									  @RequestParam("password") String password);
}
