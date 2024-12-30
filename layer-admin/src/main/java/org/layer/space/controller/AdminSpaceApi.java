package org.layer.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.retrospect.controller.dto.AdminRetrospectCountGetResponse;
import org.layer.space.controller.dto.AdminSpaceCountGetResponse;
import org.layer.space.controller.dto.AdminSpacesGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Tag(name = "[ADMIN] 스페이스 데이터", description = "스페이스 관련 api")
public interface AdminSpaceApi {
    @Operation(summary = "스페이스 관련 데이터 조회", description = "")
    @Parameters({
            @Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true),
            @Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true),
            @Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true)
    })
    ResponseEntity<AdminSpacesGetResponse> getSpaceData(@RequestParam("startDate") LocalDateTime startDate,
                                                        @RequestParam("endDate") LocalDateTime endDate, @RequestParam("password") String password);

    @Operation(summary = "스페이스 개수 조회", description = "특정 기간내에 만들어진 스페이스 개수를 조회합니다. (우리 팀원이 만든 스페이스는 제외)")
    @Parameters({
            @Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true, schema = @Schema(type = "string")),
            @Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true, schema = @Schema(type = "string")),
            @Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true, schema = @Schema(type = "string", format = "string"))})
    ResponseEntity<AdminSpaceCountGetResponse> getSpaceCount(@RequestParam("startDate") LocalDateTime startDate,
                                                             @RequestParam("endDate") LocalDateTime endDate, @RequestParam("password") String password);


    @Operation(summary = "특정 스페이스 내 회고 개수 조회", description = "특정 기간내에 특정 스페이스 안에서 시작된 회고 개수를 조회합니다.")
    @Parameters({
            @Parameter(name = "startDate", description = "검색 시작 시간", example = "2024-09-05T15:30:45", required = true, schema = @Schema(type = "string")),
            @Parameter(name = "endDate", description = "검색 종료 시간", example = "2024-09-13T15:30:45", required = true, schema = @Schema(type = "string")),
            @Parameter(name = "password", description = "비밀번호 [카톡방으로 공유]", example = "[카톡방으로 공유]", required = true, schema = @Schema(type = "string", format = "string"))})
    ResponseEntity<AdminRetrospectCountGetResponse> getRetrospectCountInSpace(@RequestParam("startDate") LocalDateTime startDate,
                                                                              @RequestParam("endDate") LocalDateTime endDate,
                                                                              @PathVariable("spaceId") Long spaceId,
                                                                              @RequestParam("password") String password);

}
