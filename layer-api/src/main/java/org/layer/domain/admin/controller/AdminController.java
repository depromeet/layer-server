package org.layer.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.layer.domain.admin.controller.dto.AdminRetrospectCountGetResponse;
import org.layer.domain.admin.controller.dto.AdminRetrospectsGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpaceCountGetResponse;
import org.layer.domain.admin.controller.dto.AdminSpacesGetResponse;
import org.layer.domain.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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

	@Override
	@GetMapping("/space/count/user-only")
	public ResponseEntity<AdminSpaceCountGetResponse> getSpaceCount(
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate,
			@RequestParam("password") String password) {
		return ResponseEntity.ok(adminService.getSpaceCount(startDate, endDate, password));
	}

	@Override
	@GetMapping("/retrospect/count/user-only")
	public ResponseEntity<AdminRetrospectCountGetResponse> getRetrospectCount(
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate,
			@RequestParam("password") String password) {

		return ResponseEntity.ok(adminService.getRetrospectCount(startDate, endDate, password));

	}


}
