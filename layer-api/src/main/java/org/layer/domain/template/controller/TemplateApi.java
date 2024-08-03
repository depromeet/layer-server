package org.layer.domain.template.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.domain.template.controller.dto.TemplateDetailInfoResponse;
import org.layer.domain.template.controller.dto.TemplateSimpleInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "기본 템플릿 조회 API (ex. KPT, 4L과 같은 고정 템플릿)")
public interface TemplateApi {
    @Operation(summary = "템플릿 간단 정보 단건 조회", method = "GET", description = """
            특정 템플릿의 간단 정보를 조회합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TemplateSimpleInfoResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<TemplateSimpleInfoResponse> getTemplateSimpleInfo(@PathVariable Long templateId);


    @Operation(summary = "템플릿 상세 정보 단건 조회", method = "GET", description = """
            특정 템플릿의 설명이 담긴 상세 정보를 조회합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TemplateDetailInfoResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<TemplateDetailInfoResponse> getTemplateDetailInfo(@PathVariable Long templateId);



    @Operation(summary = "모든 템플릿 간단 정보 조회", method = "GET", description = """
            모든 템플릿의 간단 정보를 조회합니다.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TemplateSimpleInfoResponse.class)
                            )
                    }
            )
    }
    )
    ResponseEntity<List<TemplateSimpleInfoResponse>> getAllTemplates();
}
