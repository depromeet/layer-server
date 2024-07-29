package org.layer.domain.template.controller.dto;

import lombok.Builder;
import org.layer.domain.template.entity.TemplateQuestion;

@Builder
public record TemplateDetailQuestionResponse(Long id, String entryWord, String description) {
    public static TemplateDetailQuestionResponse toResponse(TemplateQuestion templateQuestion) {
        return TemplateDetailQuestionResponse.builder()
                .id(templateQuestion.getId())
                .entryWord(templateQuestion.getEntryWord())
                .description(templateQuestion.getDescription())
                .build();
    }
}
