package org.layer.domain.actionItem.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.common.annotation.MemberId;
import org.layer.domain.actionItem.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "액션아이템 API")
public interface ActionItemApi {
    @Operation(summary = "액션 아이템 생성", method = "POST", description = """
            특정 회고와 매핑되는 액션 아이템을 생성합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateActionItemResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<CreateActionItemResponse> createActionItem(@MemberId Long memberId,
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
    ResponseEntity<CreateActionItemResponse> memberActionItem(@MemberId Long memberId);

    @Operation(summary = "팀의 액션 아이템 조회", method = "GET", description = """
            팀 아이디로 팀의 모든 액션아이템을 조회합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TeamActionItemResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<CreateActionItemResponse> teamActionItem(@MemberId Long memberId,
                                                            @Validated @RequestBody TeamActionItemRequest teamActionItemRequest);

    @Operation(summary = "액션 아이템 삭제", method = "DELETE", description = """
            액션 아이템을 삭제합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    }
    )
    ResponseEntity<CreateActionItemResponse> deleteActionItem(@MemberId Long memberId,
                                                              @Validated @RequestBody DeleteActionItemRequest deleteActionItemRequest);
}