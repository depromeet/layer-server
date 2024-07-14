package org.layer.domain.space.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.common.annotation.MemberId;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
    ResponseEntity<SpaceResponse.SpacePage> getMySpaceList(@MemberId Long memberId, @ModelAttribute @Validated SpaceRequest.GetSpaceRequest getSpaceRequest);

    @Operation(summary = "스페이스 생성하기", method = "PUT", description = """
            스페이스를 생성합니다. <br />
            생성 성공 시 아무것도 반환하지 않습니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    }
            )
    }
    )
    ResponseEntity<Void> createSpace(@MemberId Long memberId, @RequestBody @Validated SpaceRequest.CreateSpaceRequest createSpaceRequest);

    @Operation(summary = "스페이스 수정하기", method = "POST", description = """
            스페이스를 수정합니다. <br />
            생성 성공 시 아무것도 반환하지 않습니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "202",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    }
            )
    }
    )
    ResponseEntity<Void> updateSpace(@MemberId Long memberId, @RequestBody @Validated SpaceRequest.UpdateSpaceRequest updateSpaceRequest);

    @Operation(summary = "스페이스 단건 조회하기", method = "GET", description = """
            스페이스 아이디를 통해 하나의 스페이스를 조회합니다.
            내가 속하지 않은 공간만 조회할 수 있습니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SpaceResponse.SpaceWithMemberCountInfo.class))
            })
    })
    ResponseEntity<SpaceResponse.SpaceWithMemberCountInfo> getSpaceById(@MemberId Long memberId, @PathVariable Long spaceId);


    @Operation(summary = "스페이스 입장하기", method = "POST", description = """
            공유받은 링크를 통해 스페이스에 입장합니다.
            category가 TEAM인 경우만 가능합니다.
            회원이 아닐 경우 입장할 수 없습니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "202", content = {
                    @Content(mediaType = "application/json", schema = @Schema)
            }),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(),
                            examples = {
                                    @ExampleObject(
                                            name = "SPACE_ALREADY_JOINED",
                                            description = "이미 참여중인 스페이스",
                                            value = """
                                                    {
                                                       "name": "SPACE_ALREADY_JOINED",
                                                       "message": "이미 가입한 스페이스에요."
                                                     }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "SPACE_NOT_FOUND",
                                            description = "존재하지 않는 스페이스",
                                            value = """
                                                    {
                                                        "name": "SPACE_NOT_FOUND",
                                                        "message": "스페이스를 찾을 수 없어요."
                                                    }
                                                    """
                                    )


                            }
                    )

            )
    })
    ResponseEntity<Void> createMemberSpace(@MemberId Long memberId, @PathVariable Long spaceId);

    @Operation(summary = "스페이스 탈퇴하기", method = "POST", description = """
            내가 속한 스페이스를 탈퇴합니다.
            category가 TEAM인 경우만 가능합니다.
            회원이 아닐 경우 입장할 수 없습니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "202", content = {
                    @Content(mediaType = "application/json", schema = @Schema)
            }),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(),
                            examples = {
                                    @ExampleObject(
                                            name = "SPACE_NOT_JOINED",
                                            description = "참여중인 스페이스가 아닐경우.",
                                            value = """
                                                    {
                                                       "name": "SPACE_NOT_JOINED",
                                                       "message": "참여중인 스페이스가 아니에요."
                                                     }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "SPACE_NOT_FOUND",
                                            description = "존재하지 않는 스페이스",
                                            value = """
                                                    {
                                                        "name": "SPACE_NOT_FOUND",
                                                        "message": "스페이스를 찾을 수 없어요."
                                                    }
                                                    """
                                    )


                            }
                    )

            )
    })
    ResponseEntity<Void> removeMemberSpace(@MemberId Long memberId, @PathVariable Long spaceId);
}
