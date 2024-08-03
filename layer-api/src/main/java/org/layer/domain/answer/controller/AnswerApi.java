package org.layer.domain.answer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.layer.common.annotation.MemberId;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.layer.domain.answer.controller.dto.response.AnswerListGetResponse;
import org.layer.domain.answer.controller.dto.response.TemporaryAnswerListResponse;
import org.layer.domain.answer.controller.dto.response.WrittenAnswerListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회고 작성", description = "회고 작성 관련 API")
public interface AnswerApi {

    @Operation(summary = "회고 작성", description = "")
    ResponseEntity<Void> createAnswer(@PathVariable("spaceId") Long spaceId,
                                      @PathVariable("retrospectId") Long retrospectId,
                                      @RequestBody @Valid AnswerListCreateRequest request, @MemberId Long memberId);

    @Operation(summary = "임시 저장된 회고 조회", description = "")
    ResponseEntity<TemporaryAnswerListResponse> getTemporaryAnswer(@PathVariable("spaceId") Long spaceId,
                                                                   @PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId);

    @Operation(summary = "회고 분석 조회", description = "회고 분석 결과를 조회합니다.")
    ResponseEntity<AnswerListGetResponse> getAnalyzeAnswer(@PathVariable("spaceId") Long spaceId,
                                                           @PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId);

    @Operation(summary = "회고 마감 전 내가 작성한 회고 답변 조회하기", description = """
            회고가 마감되지 않았다면, 내가 작성한 회고 답변을 조회합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = WrittenAnswerListResponse.class))
            })
    })
    ResponseEntity<WrittenAnswerListResponse> getWrittenAnswersBeforeRetrospectIsDone(@PathVariable("spaceId") Long spaceId, @PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId);
}
