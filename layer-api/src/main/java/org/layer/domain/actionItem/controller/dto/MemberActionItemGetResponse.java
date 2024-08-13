package org.layer.domain.actionItem.controller.dto;

import jakarta.validation.constraints.NotNull;
import org.layer.domain.actionItem.dto.MemberActionItemResponse;

import java.util.List;

public record MemberActionItemGetResponse(@NotNull List<MemberActionItemResponse> actionItems) {
}
