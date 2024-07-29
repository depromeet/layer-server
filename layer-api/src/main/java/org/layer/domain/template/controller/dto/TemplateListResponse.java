package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.template.entity.Template;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public record TemplateListResponse(
        @Schema(description = "템플릿") List<TemplateSimpleInfoResponse> templateInfoList,
        @Schema(description = "마지막 페이지 여부") Boolean last
) {
    public static TemplateListResponse toResponse(Slice<Template> templateSlice) {
        List<TemplateSimpleInfoResponse> simpleInfoList = templateSlice.getContent().stream()
                .map(TemplateSimpleInfoResponse::toResponse)
                .collect(Collectors.toList());
        return new TemplateListResponse(simpleInfoList, templateSlice.isLast());
    }
}
