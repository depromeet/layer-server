package org.layer.domain.stats;

import org.layer.annotation.MemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatsSpaceController implements StatsSpaceApi {

	private final StatsRetrospectService statsRetrospectService;

	@Override
	@PostMapping("/stats/spaces/{spaceId}/click")
	public ResponseEntity<Void> clickSpace(@PathVariable Long spaceId, @MemberId Long memberId){

		statsRetrospectService.clickSpace(spaceId, memberId);

		return ResponseEntity.ok().body(null);
	}
}
