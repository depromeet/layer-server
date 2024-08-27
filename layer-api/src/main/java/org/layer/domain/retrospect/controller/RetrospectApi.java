package org.layer.domain.retrospect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.layer.common.annotation.MemberId;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.controller.dto.request.RetrospectUpdateRequest;
import org.layer.domain.retrospect.controller.dto.response.RetrospectCreateResponse;
import org.layer.domain.retrospect.controller.dto.response.RetrospectListGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회고", description = "회고 관련 API")
public interface RetrospectApi {

	@Operation(summary = "회고 생성", description = "회고 생성 이후 생성된 회고의 아이디를 응답합니다.")
	ResponseEntity<RetrospectCreateResponse> createRetrospect(@PathVariable("spaceId") Long spaceId,
		@RequestBody @Valid RetrospectCreateRequest request, @MemberId Long memberId);

	@Operation(summary = "회고 목록 조회", description = "특정 팀 스페이스에서 작성했던 회고 목록을 보는 기능입니다.")
	ResponseEntity<RetrospectListGetResponse> getRetrospects(@PathVariable("spaceId") Long spaceId,
		@MemberId Long memberId);

	@Operation(summary = "회고 수정", description = "특정 팀 스페이스에서 작성했던 회고를 수정하는 기능입니다.")
	ResponseEntity<RetrospectListGetResponse> updateRetrospect(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @RequestBody @Valid RetrospectUpdateRequest request, @MemberId Long memberId);

	@Operation(summary = "회고 삭제", description = "특정 팀 스페이스에서 작성했던 회고를 삭제하는 기능입니다.")
	ResponseEntity<RetrospectListGetResponse> deleteRetrospect(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId);

	@Operation(summary = "회고 마감", description = "특정 팀 스페이스에서 작성했던 회고를 마감하는 기능입니다. </br> Note: 스페이스 내 모든 인원이 작성해야 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "회고 마감 성공",
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "회고 마감 실패",
			content = @Content(mediaType = "application/json", examples = {
				@ExampleObject(name = "회고 마감 실패", value = """
                                    {
                                      "name": "NOT_COMPLETE_RETROSPECT_MEMBER",
                                      "message": "회고를 작성하지 않은 팀원이 있습니다."
                                    }
                                    """
				)
			}))
	})
	ResponseEntity<Void> closeRetrospect(
		@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId,
		@MemberId Long memberId);
}
