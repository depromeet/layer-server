package org.layer.admin.common;

import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectAnswerRepository;
import org.layer.admin.retrospect.repository.AdminRetrospectHistoryRepository;
import org.layer.admin.space.repository.AdminSpaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommonController {

	private final AdminMemberRepository adminMemberRepository;
	private final AdminSpaceRepository adminSpaceRepository;
	private final AdminRetrospectHistoryRepository adminRetrospectHistoryRepository;
	private final AdminRetrospectAnswerRepository adminRetrospectAnswerRepository;

	@GetMapping("/admin/outline")
	public ResponseEntity<OutlineResponse> getOutline() {

		return ResponseEntity.ok(
			new OutlineResponse(
				adminMemberRepository.countExcluding(ExcludedMembers.ID_LIST),
				adminSpaceRepository.countExcluding(ExcludedMembers.ID_LIST),
				adminRetrospectHistoryRepository.countExcluding(ExcludedMembers.ID_LIST),
				adminRetrospectAnswerRepository.countExcluding(ExcludedMembers.ID_LIST)
			)
		);
	}
}
