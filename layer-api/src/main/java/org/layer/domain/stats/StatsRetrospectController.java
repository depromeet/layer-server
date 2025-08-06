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
	@PostMapping("/stats/retrospects/{retrospectId}/click")
	public ResponseEntity<Void> clickSpace(@PathVariable Long retrospectId, @MemberId Long memberId){

		statsRetrospectService.clickRetrospect(retrospectId, memberId);

		return ResponseEntity.ok().body(null);
	}
}
