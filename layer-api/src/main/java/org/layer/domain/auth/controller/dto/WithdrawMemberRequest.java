package org.layer.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "회원 탈퇴하기 요청 DTO")
public record WithdrawMemberRequest(
        @NotNull
        @Size(min = 3, max = 3)
        @Schema(title = "삭제 이유 체크박스")
        boolean[] booleans,

        @NotNull
        @Schema(title = "서비스 이용 경험")
        String description
) {
}
