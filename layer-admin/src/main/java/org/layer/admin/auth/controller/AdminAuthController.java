package org.layer.admin.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminAuthController {

	@GetMapping("/admin/login")
	public ResponseEntity<String> login() {
		// 인터셉터 단에서 인증을 처리하므로, 여기서는 단순히 성공 메시지를 반환합니다.
		return ResponseEntity.ok("Admin login successful");
	}
}
