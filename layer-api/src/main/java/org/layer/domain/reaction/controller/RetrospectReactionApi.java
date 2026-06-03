package org.layer.domain.reaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.layer.annotation.MemberId;
import org.layer.domain.reaction.controller.dto.request.RetrospectReactionCreateRequest;
import org.layer.domain.reaction.controller.dto.response.RetrospectReactionListGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회고 반응", description = "회고 답변에 대한 반응 관련 API")
public interface RetrospectReactionApi {

    @Operation(summary = "회고 반응 생성", description = "특정 회고 답변에 대한 반응을 생성합니다. 하나의 답변에 하나의 반응만 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회고 반응 생성 성공")
    })
    ResponseEntity<Void> createReaction(
            @PathVariable("spaceId") Long spaceId,
            @PathVariable("retrospectId") Long retrospectId,
            @RequestBody @Valid RetrospectReactionCreateRequest request,
            @MemberId Long memberId
    );

    @Operation(summary = "회고 반응 삭제", description = "본인이 반응한 회고 반응을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회고 반응 삭제 성공")
    })
    ResponseEntity<Void> deleteReaction(
            @PathVariable("spaceId") Long spaceId,
            @PathVariable("retrospectId") Long retrospectId,
            @PathVariable("retrospectReactionId") Long retrospectReactionId,
            @MemberId Long memberId
    );

    @Operation(summary = "회고 반응 조회", description = "특정 회고의 모든 답변에 대한 반응을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RetrospectReactionListGetResponse.class))
            })
    })
    ResponseEntity<RetrospectReactionListGetResponse> getRetrospectReactions(
            @PathVariable("spaceId") Long spaceId,
            @PathVariable("retrospectId") Long retrospectId,
            @MemberId Long memberId
    );
}
