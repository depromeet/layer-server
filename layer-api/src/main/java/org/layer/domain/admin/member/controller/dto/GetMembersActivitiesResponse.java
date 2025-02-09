package org.layer.domain.admin.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name = "GetMembersActivitiesResponse", description = "회원 활동 목록 Dto")
public record GetMembersActivitiesResponse(
	@NotNull
	@Schema(description = "회원 활동 목록", example = "")
	List<GetMemberActivityResponse> responses

) {
}
