package org.layer.domain.member.controller.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "정보가 변경된 회원")
public record UpdateMemberInfoResponse(@NotNull
                                       @Schema(description = "정보가 변경된 회원 ID")
                                       Long memberId,
                                       @NotNull
                                       @Schema(description = "변경된 이름")
                                       String name,
                                       @Schema(description = "변경된 이미지 url")
                                       String profileImageUrl) {
}
