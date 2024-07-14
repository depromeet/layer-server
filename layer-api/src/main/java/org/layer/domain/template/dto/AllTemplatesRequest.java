package org.layer.domain.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record AllTemplatesRequest(@Schema(description = "페이지 번호(0번부터 시작)") int page,
                                  @Schema(description = "한 페이지에 보여줄 개수") int size) {
}
