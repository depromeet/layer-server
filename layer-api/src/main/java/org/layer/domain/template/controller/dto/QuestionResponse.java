package org.layer.domain.template.controller.dto;

import jakarta.validation.constraints.NotNull;
import org.layer.domain.question.enums.QuestionType;

public record QuestionResponse(
        @NotNull
        String question,
        @NotNull
        QuestionType questionType
) {
}
