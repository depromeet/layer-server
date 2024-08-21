package org.layer.domain.actionItem.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ActionItemCreateResponse(@Schema(description = "실행 목표 ID")
                                       Long actionItemId) {
}
