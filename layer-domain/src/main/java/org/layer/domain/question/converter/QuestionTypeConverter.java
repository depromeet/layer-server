package org.layer.domain.question.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.layer.domain.question.entity.QuestionType;

import java.util.stream.Stream;

@Converter
public class QuestionTypeConverter implements AttributeConverter<QuestionType, String> {

    @Override
    public String convertToDatabaseColumn(QuestionType blockType) {
        return blockType.getStyle();
    }

    @Override
    public QuestionType convertToEntityAttribute(String questionType) {
        if (questionType == null) {
            return null;
        }
        return Stream.of(QuestionType.values()).filter(t -> t.getStyle().equals(questionType)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
