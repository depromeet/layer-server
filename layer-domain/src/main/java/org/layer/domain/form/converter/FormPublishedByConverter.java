package org.layer.domain.form.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.layer.domain.form.enums.FormPublishedBy;

import java.util.stream.Stream;

@Converter
public class FormPublishedByConverter implements AttributeConverter<FormPublishedBy,String> {

    @Override
    public String convertToDatabaseColumn(FormPublishedBy formPublishType) {
        return formPublishType.getCode();
    }

    @Override
    public FormPublishedBy convertToEntityAttribute(final String code) {
        if(code == null){
            return null;
        }
        return Stream.of(FormPublishedBy.values()).filter(t -> t.getCode().equals(code)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
