package org.layer.domain.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.domain.template.entity.Template;
import org.layer.domain.template.entity.TemplateQuestion;

import java.util.List;


@Builder
public record TemplateQuestionsResponse(
        @Schema(description = "템플릿 ID")
        @NotNull
        Long id,
        @Schema(description = "템플릿 제목", example = "빠르고 효율적인 회고")
        @NotNull
        String title,
        @Schema(description = "템플릿 명칭", example = "KPT 회고")
        @NotNull
        String templateName,
        @Schema(description = "템플릿에 속한 질문 리스트")
        List<TemplateQuestion> questionList
) {

        public static TemplateQuestionsResponse toResponse(Template template, List<TemplateQuestion> questionList) {
                return TemplateQuestionsResponse.builder()
                        .id(template.getId())
                        .title(template.getTitle())
                        .templateName(template.getTemplateName())
                        .questionList(questionList)
                        .build();
        }
}
