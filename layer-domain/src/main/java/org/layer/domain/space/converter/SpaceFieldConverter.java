package org.layer.domain.space.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.layer.domain.space.entity.SpaceField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SpaceFieldConverter implements AttributeConverter<List<SpaceField>, String> {

    @Override
    public String convertToDatabaseColumn(List<SpaceField> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<SpaceField> convertToEntityAttribute(String fieldsOfCommaSeperated) {
        if (fieldsOfCommaSeperated == null || fieldsOfCommaSeperated.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(fieldsOfCommaSeperated.split(","))
                .map(String::trim)
                .map(SpaceField::valueOf)
                .collect(Collectors.toList());
    }
}