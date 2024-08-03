package org.layer.domain.form.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
@Schema(name = "CustomFormGetResponse", description = "커스텀 템플릿 조회 응답 Dto")
public record CustomTemplateListResponse(
        @NotNull
        @Schema(description = "스페이스 아이디", example = "1")
        Long spaceId,
        @NotNull
        @Schema(description = "커스템 템플릿 객체 목록")
        Page<CustomTemplateResponse> customTemplateList

) {
}
