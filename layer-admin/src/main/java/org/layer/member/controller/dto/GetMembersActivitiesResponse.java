package org.layer.member.controller.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "GetMembersActivitiesResponse", description = "회원 활동 목록 Dto")
public record GetMembersActivitiesResponse(
	@NotNull
	@Schema(description = "회원 활동 목록", example = "")
	List<GetMemberActivityResponse> responses

) {
}
