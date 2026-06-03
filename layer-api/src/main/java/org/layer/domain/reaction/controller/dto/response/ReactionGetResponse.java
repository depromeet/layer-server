package org.layer.domain.reaction.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.reaction.entity.Reaction;

@Schema(name = "ReactionGetResponse", description = "반응 조회 응답 DTO")
public record ReactionGetResponse(
        @Schema(description = "반응 ID", example = "1")
        Long id,

        @Schema(description = "반응 이미지 URL", example = "https://example.com/emoji.png")
        String imgUrl
) {
    public static ReactionGetResponse from(Reaction reaction) {
        return new ReactionGetResponse(reaction.getId(), reaction.getImgUrl());
    }
}
