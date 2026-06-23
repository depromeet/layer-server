package org.layer.domain.reaction.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.domain.reaction.entity.EmojiCode;
import org.layer.domain.reaction.entity.RetrospectReaction;

@Schema(name = "RetrospectReactionElementResponse", description = "회고 반응 요소 응답 DTO")
public record RetrospectReactionElementResponse(
        @Schema(description = "회고 반응 ID", example = "1")
        Long retrospectReactionId,

        @Schema(description = "이모지 코드", example = "LEC01")
        String emojiCode,

        @Schema(description = "이모지 설명", example = "대단해")
        String description,

        @Schema(description = "반응한 멤버 ID", example = "3")
        Long memberId,

        @Schema(description = "반응한 멤버 이름", example = "홍길동")
        String memberName,

        @Schema(description = "반응한 멤버 프로필 이미지 URL", example = "https://example.com/profile.png")
        String memberProfileImgUrl
) {
    public static RetrospectReactionElementResponse of(
            RetrospectReaction retrospectReaction,
            EmojiCode emojiCode,
            String memberName,
            String memberProfileImgUrl
    ) {
        return new RetrospectReactionElementResponse(
                retrospectReaction.getId(),
                emojiCode.name(),
                emojiCode.getDescription(),
                retrospectReaction.getMemberId(),
                memberName,
                memberProfileImgUrl
        );
    }
}
