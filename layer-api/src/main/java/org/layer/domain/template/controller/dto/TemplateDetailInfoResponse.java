package org.layer.domain.template.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.form.entity.Form;
import org.layer.domain.template.entity.TemplateMetadata;

import java.util.List;
import java.util.Optional;

@Builder
@Schema
public record TemplateDetailInfoResponse(
        @Schema(description = "템플릿 ID", example = "1")
        @NotNull
        Long id,
        @Schema(description = "템플릿 제목", example = "빠르고 효율적인 회고")
        @NotNull
        String title, // ex. 빠르고 효율적인 회고
        @Schema(description = "템플릿 명칭", example = "KPT 회고")
        @NotNull
        String templateName, // ex. KPT 회고
        @Schema(description = "템플릿 대표 사진", example = "[이미지 url]")
        // @NotNull
        String templateImageUrl,
        @Schema(description = "회고에 대한 설명", example = "회고 내용을 Keep, Problem, Try 세가지 관점으로 분류하여... [생략]")
        @NotNull
        String introduction, // 회고에 대한 설명
        @Schema(description = "회고 팁 제목", example = "회고는 빠르고 간단하게!")
        @NotNull
        String tipTitle, // ex) 회고는 빠르고 간단하게!
        @Schema(description = "회고 팁 설명", example = "KPT 회고는 짧은 시간에 구성원의 생각을... [생략]")
        @NotNull
        String tipDescription, // 팁에 대한 설명

        @Schema(description = "템플릿 질문과 그 설명 리스트")
        @NotNull
        List<TemplateDetailQuestionResponse> templateDetailQuestionList, // 질문(회고 과정)에 대한 설명

        @Schema(description = "템플릿 목적 태그 리스트")
        List<TemplatePurposeResponse> templatePurposeResponseList // 질문(회고 과정)에 대한 설명

) {
    public static TemplateDetailInfoResponse toResponse(Form form, Optional<TemplateMetadata> templateMetadata, List<TemplateDetailQuestionResponse> templateQuestionList, List<TemplatePurposeResponse> templatePurposeResponseList) {
        return TemplateDetailInfoResponse.builder()
                .id(form.getId())
                .title(form.getTitle())
                .templateName(form.getFormTag().getTag())
                .templateImageUrl(templateMetadata.map(TemplateMetadata::getTemplateImageUrl).orElse(null))
                .introduction(form.getIntroduction())
                .tipTitle(templateMetadata.map(TemplateMetadata::getTipTitle).orElse(null))
                .tipDescription(templateMetadata.map(TemplateMetadata::getTipDescription).orElse(null))
                .templateDetailQuestionList(templateQuestionList)
                .templatePurposeResponseList(templatePurposeResponseList)
                .build();
    }
}