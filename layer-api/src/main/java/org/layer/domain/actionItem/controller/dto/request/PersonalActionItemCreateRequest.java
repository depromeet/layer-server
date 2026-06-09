package org.layer.domain.actionItem.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "개인 실행 목표 생성 요청 DTO")
public record PersonalActionItemCreateRequest(
        @Schema(description = "실행 목표 내용", example = "다음 회고까지 문서 정리하기")
        @NotBlank
        String content
) {}
