package org.layer.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "피드백 남기기 요청 DTO")
public record CreateFeedbackRequest(
        @Schema(title = "서비스 만족도 1 - 5")
        int satisfaction,

        @Schema(title = "자세한 피드백 정보")
        String description
) {
}
