package org.layer.domain.actionItem.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Schema(description = "실행 목표 리스트 편집 요청 dto. 리스트의 순서를 편집된 순서와 일치하게 넘겨주세요!")
public record ActionItemUpdateRequest(@NotNull
                                      @Schema(description = "실행 목표 리스트")
                                      List<ActionItemUpdateElementRequest> actionItems) {

    public record ActionItemUpdateElementRequest(
        @Schema(description = "실행 목표 id (신규 생성시 null)")
        Long id,

        @Schema(description = "변경된 실행 목표 내용")
        @NotNull
        String content
    ) {}
}
