package org.layer.domain.block.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.layer.domain.block.enums.BlockType;


import java.util.stream.Stream;

@Converter
public class BlockStyleConverter implements AttributeConverter<BlockType, String> {

    @Override
    public String convertToDatabaseColumn(BlockType blockType) {
        return blockType.getStyle();
    }

    @Override
    public BlockType convertToEntityAttribute(String blockStyle) {
        if(blockStyle == null){
            return null;
        }
        return Stream.of(BlockType.values()).filter(t -> t.getStyle().equals(blockStyle)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
