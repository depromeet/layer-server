package org.layer.domain.template.dto;

import lombok.Builder;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.template.entity.TemplateQuestion;
@Builder
public record TemplateQuestionResponse(Long id, String content, QuestionType questionType) {
    public static TemplateQuestionResponse toResponse(TemplateQuestion templateQuestion) {
        return TemplateQuestionResponse.builder()
                .id(templateQuestion.getId())
                .content(templateQuestion.getContent())
                .questionType(templateQuestion.getQuestionType())
                .build();
    }
}
