package org.layer.domain.stats;

import org.layer.annotation.MemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "📊어드민")
public interface StatsRetrospectApi {

	@Operation(summary = "회고 노출 이벤트", description = "회고 목록에 노출될 때 해당 API를 호출합니다.")
	ResponseEntity<Void> impressionRetrospect(@MemberId Long memberId);

	@Operation(summary = "회고 클릭 이벤트", description = "특정 회고 클릭했을 때 해당 API를 호출합니다.")
	ResponseEntity<Void> clickRetrospect(@PathVariable Long retrospectId, @MemberId Long memberId);
}
