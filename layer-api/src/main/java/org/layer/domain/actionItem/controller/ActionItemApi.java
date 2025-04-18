package org.layer.domain.actionItem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.annotation.MemberId;
import org.layer.domain.actionItem.controller.dto.request.ActionItemCreateBySpaceIdRequest;
import org.layer.domain.actionItem.controller.dto.request.ActionItemCreateRequest;
import org.layer.domain.actionItem.controller.dto.request.ActionItemUpdateRequest;
import org.layer.domain.actionItem.controller.dto.response.*;
import org.layer.domain.actionItem.dto.MemberActionItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "실행 목표 API")
public interface ActionItemApi {
    @Operation(summary = "실행 목표 생성", method = "POST", description = """
            특정 회고와 매핑되는 실행 목표를 생성합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201")
    }
    )
    ResponseEntity<ActionItemCreateResponse> createActionItem(@MemberId Long memberId,
                                                              @Validated @RequestBody ActionItemCreateRequest actionItemCreateRequest);


    @Operation(summary = "개인의 액션 아이템 조회", method = "GET", description = """
            회원이 속한 스페이스의 모든 실행 목표를 회고 별로 조회합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MemberActionItemResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<MemberActionItemGetResponse> memberActionItem(@MemberId Long currentMemberId);

    @Operation(summary = "팀의 실행 목표 조회", method = "GET", description = """
            팀 아이디로 팀의 모든 실행 목표를 회고 별로 조회합니다.
            정렬 기준: 최신순(데드라인 내림차순)
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SpaceActionItemElementResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<SpaceRetrospectActionItemGetResponse> teamActionItem(@MemberId Long memberId,
                                                                        @PathVariable Long spaceId);


    @Operation(summary = "스페이스의 가장 최근 회고의 실행 목표 조회", method = "GET", description = """
             특정 스페이스에서 완료된 가장 최근 회고(데드라인 기준)를 찾고, 그 회고의 실행 목표 목록을 조회합니다.
             해당 스페이스에서 아직 완료된 회고가 없다면 null이 반환됩니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SpaceActionItemElementResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<SpaceActionItemGetResponse> spaceRecentActionItem(@MemberId Long memberId,
                                                                     @PathVariable Long spaceId);

    @Operation(summary = "실행 목표 삭제", method = "DELETE", description = """
            실행 목표를 삭제합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    }
    )
    ResponseEntity<Void> deleteActionItem(@MemberId Long memberId, @PathVariable("actionItemId") Long actionItemId);


    @Operation(summary = "실행 목표 편집", method = "PATCH", description = """
            특정 회고의 실행 목표 리스트를 편집합니다. 요청 데이터 리스트의 순서를 편집된 순서와 일치하게 넘겨주세요!
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    }
    )
    ResponseEntity<Void> updateActionItem(@MemberId Long memberId,
                                          @PathVariable("retrospectId") Long retrospectId,
                                          @RequestBody ActionItemUpdateRequest actionItemUpdateRequest);

    @Operation(summary = "스페이스 아이디로 실행 목표 생성", method = "POST", description = """
            특정 스페이스에서 "실행 중"인 회고(=가장 최근 종료된 회고)
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201")
    }
    )
    ResponseEntity<ActionItemCreateResponse> createActionItemBySpaceId(@MemberId Long memberId,
                                                   @Validated @RequestBody ActionItemCreateBySpaceIdRequest actionItemCreateRequest);
}