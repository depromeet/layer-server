package org.layer.admin.common;

import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectAnswerRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectRepository;
import org.layer.admin.space.repository.AdminSpaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {

	// 간단한 기능이기에 바로 repository를 사용합니다.
	private final AdminMemberRepository adminMemberRepository;
	private final AdminSpaceRepository adminSpaceRepository;
	private final AdminRetrospectRepository adminRetrospectRepository;
	private final AdminRetrospectAnswerRepository adminRetrospectAnswerRepository;


	@GetMapping("/admin/outline")
	public ResponseEntity<OutlineResponse> getOutline() {

		return ResponseEntity.ok(
			new OutlineResponse(
				adminMemberRepository.count(),
				adminSpaceRepository.count(),
				adminRetrospectRepository.count(),
				adminRetrospectAnswerRepository.count()
			)
		);
	}
}
