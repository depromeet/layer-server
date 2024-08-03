package org.layer.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "변경할 회원 정보")
public record UpdateMemberInfoRequest(@NotNull
                                       @Schema(description = "변경할 이름")
                                       String name,
                                       @Schema(description = "변경할 이미지 url")
                                       String profileImageUrl) {
}
