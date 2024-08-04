package org.layer.domain.actionItem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.common.annotation.MemberId;
import org.layer.domain.actionItem.controller.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "액션아이템 API")
public interface ActionItemApi {
    @Operation(summary = "액션 아이템 생성", method = "POST", description = """
            특정 회고와 매핑되는 액션 아이템을 생성합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201")
    }
    )
    ResponseEntity<Void> createActionItem(@MemberId Long memberId,
                                                              @Validated @RequestBody CreateActionItemRequest createActionItemRequest);


    @Operation(summary = "개인의 액션 아이템 조회", method = "GET", description = """
            회원 아이디로 개인이 작성한 모든 액션아이템을 조회합니다.
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
    ResponseEntity<MemberActionItemResponse> memberActionItem(@MemberId Long currentMemberId);

    @Operation(summary = "팀의 액션 아이템 조회", method = "GET", description = """
            팀 아이디로 팀의 모든 액션아이템을 회고 별로 조회합니다.
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
    ResponseEntity<SpaceRetrospectActionItemResponse> teamActionItem(@MemberId Long memberId,
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
    ResponseEntity<SpaceActionItemResponse> spaceRecentActionItem(@MemberId Long memberId,
                                                           @PathVariable Long spaceId);

    @Operation(summary = "액션 아이템 삭제", method = "DELETE", description = """
            액션 아이템을 삭제합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    }
    )
    ResponseEntity<Void> deleteActionItem(@MemberId Long memberId, @PathVariable("actionItemId") Long actionItemId);
}