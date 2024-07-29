package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema
public record TemplateInfoRequest(@NotNull @Schema(description = "템플릿 ID") Long id) {
}
