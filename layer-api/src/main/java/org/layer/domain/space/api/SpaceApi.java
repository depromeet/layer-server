package org.layer.domain.space.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.common.annotation.MemberId;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "스페이스 API")
public interface SpaceApi {
    @Operation(summary = "내가 속한 스페이스 조회하기", method = "GET", description = """
            내가 속한 모든 스페이스를 반환합니다.<br/>
            접속한 회원이 아닐 경우 조회가 불가능 합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SpaceResponse.SpacePage.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<SpaceResponse.SpacePage> getMySpaceList(@MemberId Long memberId, @RequestParam @Validated SpaceRequest.GetSpaceRequest getSpaceRequest);

    @Operation(summary = "스페이스 생성하기", method = "POST", description = """
            스페이스를 생성합니다. <br />
            생성 성공 시 저장된 스페이스 정보를 반환합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SpaceResponse.SpaceInfo.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<SpaceResponse.SpaceInfo> createSpace(@MemberId Long memberId, @RequestBody @Validated SpaceRequest.CreateSpaceRequest createSpaceRequest);
}
