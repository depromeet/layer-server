package org.layer.domain.reaction.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.reaction.entity.Reaction;

@Schema(name = "ReactionGetResponse", description = "반응 조회 응답 DTO")
public record ReactionGetResponse(
        @Schema(description = "반응 ID", example = "1")
        Long id,

        @Schema(description = "이모지 코드", example = "LEC01")
        String emojiCode,

        @Schema(description = "이모지 설명", example = "대단해")
        String description
) {
    public static ReactionGetResponse from(Reaction reaction) {
        return new ReactionGetResponse(
                reaction.getId(),
                reaction.getEmojiCode().name(),
                reaction.getEmojiCode().getDescription()
        );
    }
}
