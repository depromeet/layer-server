package org.layer.domain.reaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.annotation.MemberId;
import org.layer.domain.reaction.controller.dto.response.ReactionListGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "반응", description = "사용 가능한 반응 관련 API")
public interface ReactionApi {

    @Operation(summary = "사용 가능한 모든 반응 조회", description = "사용 가능한 모든 회고 반응을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ReactionListGetResponse.class))
            })
    })
    ResponseEntity<ReactionListGetResponse> getAllReactions();

    @Operation(summary = "최근 사용한 N개의 unique 반응 조회", description = "내가 최근에 사용한 N개의 중복 없는 반응을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ReactionListGetResponse.class))
            })
    })
    ResponseEntity<ReactionListGetResponse> getRecentReactions(
            @MemberId Long memberId,
            @RequestParam(name = "limit", defaultValue = "8") int limit
    );
}
