package org.layer.domain.admin.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.domain.admin.member.controller.dto.GetMembersActivitiesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "[ADMIN] 회원 서비스", description = "회원 관련 api")
public interface AdminMemberApi {

	@Operation(summary = "회원 활동 목록 조회")
	@Parameters({
		@Parameter(name = "password", description = "패스워드", example = "abcdef", required = true),
		@Parameter(name = "page", description = "페이지 수, 최솟값 1", example = "1", required = true),
		@Parameter(name = "take", description = "가져올 데이터 수", example = "20", required = true)
	})
	ResponseEntity<GetMembersActivitiesResponse> getMemberActivities(
		@RequestParam String password,
		@RequestParam int page,
		@RequestParam int take);

}
