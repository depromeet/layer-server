package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TemplateListResponse(
        @Schema(description = "템플릿") List<TemplateSimpleInfoResponse> templateInfoList,
        @Schema(description = "마지막 페이지 여부") Boolean last
) {

}
