package org.layer.domain.template.dto;

import java.util.List;

public record TemplateListResponse(
        List<TemplateSimpleInfoResponse> templateInfoList
) {
}
