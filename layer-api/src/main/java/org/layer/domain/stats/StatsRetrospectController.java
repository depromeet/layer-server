package org.layer.domain.stats;

import org.layer.annotation.MemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatsRetrospectController implements StatsRetrospectApi {

	private final StatsRetrospectService statsRetrospectService;

	@Override
	@PostMapping("/stats/retrospects/impression")
	public ResponseEntity<Void> impressionRetrospect(@MemberId Long memberId){

		statsRetrospectService.impressionRetrospect(memberId);
		return ResponseEntity.ok().body(null);
	}

	@Override
	@PostMapping("/stats/retrospects/click/{retrospectId}")
	public ResponseEntity<Void> clickRetrospect(@PathVariable Long retrospectId, @MemberId Long memberId){

		statsRetrospectService.clickRetrospect(retrospectId, memberId);
		return ResponseEntity.ok().body(null);
	}
}
