package org.layer.domain.actionItem.controller.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MemberActionItemResponse(List<MemberActionItemElementResponse> actionItemResponse) {
}
