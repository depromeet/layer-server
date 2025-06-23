package org.layer.admin.member.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.member.controller.dto.MemberSignupCountResponse;
import org.layer.admin.member.service.AdminMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminMemberController {
	private final AdminMemberService adminMemberService;

	@RequestMapping("/admin/member/signup-count")
	public ResponseEntity<List<MemberSignupCountResponse>> getMemberSignupCount(
		@RequestParam(name = "startDate") LocalDateTime startDate,
		@RequestParam(name = "endDate") LocalDateTime endDate) {

		List<MemberSignupCountResponse> response = adminMemberService.getMemberSignupCount(startDate, endDate);
		return ResponseEntity.ok().body(response);
	}
}
