package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record AllTemplatesRequest(@Schema(description = "페이지 번호(1번부터 시작)") int page,
                                  @Schema(description = "한 페이지에 보여줄 개수") int size) {
    public AllTemplatesRequest {
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 1;
        }
    }
}
