package org.layer.domain.actionItem.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteActionItemResponse(@Schema(description = "삭제된 액션 아이템 아이디") Long actionItemId) {
}
