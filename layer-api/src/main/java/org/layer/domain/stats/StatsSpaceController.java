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
	@PostMapping("/stats/spaces/impression")
	public ResponseEntity<Void> impressionSpace(@MemberId Long memberId){

		statsRetrospectService.impressionSpace(memberId);

		return ResponseEntity.ok().body(null);
	}

	@Override
	@PostMapping("/stats/spaces/click/{spaceId}")
	public ResponseEntity<Void> clickSpace(@PathVariable Long spaceId, @MemberId Long memberId){

		statsRetrospectService.clickSpace(spaceId, memberId);

		return ResponseEntity.ok().body(null);
	}
}
