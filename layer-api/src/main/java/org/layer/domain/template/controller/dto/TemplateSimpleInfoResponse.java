package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.form.entity.Form;
import org.layer.domain.template.entity.TemplateMetadata;

@Builder
@Schema(description = "템플릿 간단 정보 단건 조회 응답")
public record TemplateSimpleInfoResponse(
        @Schema(description = "기본 템플릿(폼) ID")
        @NotNull
        Long id, // form
        @Schema(description = "템플릿 제목", example = "빠르고 효율적인 회고")
        @NotNull
        String title, // form
        @Schema(description = "템플릿 명칭", example = "KPT 회고")
        @NotNull
        String templateName, // metadata

        @Schema(description = "템플릿 사진 url", example = "[url]")
        String imageUrl // metadata
        ) {

        public static TemplateSimpleInfoResponse toResponse(Form form, TemplateMetadata template) {
                return TemplateSimpleInfoResponse.builder()
                        .id(form.getId())
                        .title(form.getTitle())
                        .templateName(form.getFormTag().getTag())
                        .imageUrl(template.getTemplateImageUrl())
                        .build();
        }
}
