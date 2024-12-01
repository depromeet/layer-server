package org.layer.member.controller;

import org.layer.member.controller.dto.GetMembersActivitiesResponse;
import org.layer.member.service.AdminMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/members")
@RequiredArgsConstructor
@RestController
public class AdminMemberController implements AdminMemberApi {
	private final AdminMemberService adminMemberService;

	@Override
	@GetMapping
	public ResponseEntity<GetMembersActivitiesResponse> getMemberActivities(
		@RequestParam String password,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "20") int take) {

		return ResponseEntity.ok().body(adminMemberService.getMemberActivities(password, page, take));
	}

}
