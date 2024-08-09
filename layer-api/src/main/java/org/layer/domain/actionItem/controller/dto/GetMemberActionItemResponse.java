package org.layer.domain.actionItem.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetMemberActionItemResponse(@NotNull List<MemberActionItemResponse> actionItems) {
}
