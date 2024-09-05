package org.layer.domain.admin.controller;

import java.time.LocalDateTime;

import org.layer.common.annotation.MemberId;
import org.layer.domain.admin.controller.dto.AdminRetrospectsGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpacesGetResponse;
import org.layer.domain.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController implements AdminApi {
	private final AdminService adminService;

	@Override
	@GetMapping("/space")
	public ResponseEntity<AdminSpacesGetResponse> getSpaceData(
		@RequestParam("startDate") LocalDateTime startDate,
		@RequestParam("endDate") LocalDateTime endDate,
		@RequestParam("password") String password) {

		return ResponseEntity.ok(adminService.getSpaceData(startDate, endDate, password));
	}

	@Override
	@GetMapping("/retrospect")
	public ResponseEntity<AdminRetrospectsGetResponse> getRetrospectData(
		@RequestParam("startDate") LocalDateTime startDate,
		@RequestParam("endDate") LocalDateTime endDate,
		@RequestParam("password") String password) {


		return ResponseEntity.ok(adminService.getRetrospectData(startDate, endDate, password));
	}
}
