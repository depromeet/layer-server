package org.layer.domain.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import org.layer.domain.template.entity.Template;
import org.layer.domain.template.exception.TemplateException;

import java.util.Optional;

import static org.layer.domain.template.exception.TemplateExceptionType.INVALID_TEMPLATE;

@Builder
@Schema(description = "템플릿 간단 정보 단건 조회 응답")
public record TemplateSimpleInfoResponse(
        @Schema(description = "템플릿 ID")
        @NotNull
        Long id,
        @Schema(description = "템플릿 제목", example = "빠르고 효율적인 회고")
        @NotNull
        String title,
        @Schema(description = "템플릿 명칭", example = "KPT 회고")
        @NotNull
        String templateName,

        @Schema(description = "템플릿 사진 url", example = "[url]")
        String imageUrl
        ) {

        public static TemplateSimpleInfoResponse toResponse(Template template) {
                return Optional.ofNullable(template)
                        .map(it -> TemplateSimpleInfoResponse.builder().id(it.getId()).title(it.getTitle())
                                .templateName(it.getTemplateName()).imageUrl(it.getTemplateImageUrl()).build())
                        .orElseThrow(() -> new TemplateException(INVALID_TEMPLATE));
        }
}
