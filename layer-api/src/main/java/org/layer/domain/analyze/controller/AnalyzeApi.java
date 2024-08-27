package org.layer.domain.analyze.controller;

import java.util.List;

import org.layer.common.annotation.MemberId;
import org.layer.domain.analyze.controller.dto.response.AnalyzesGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "분석 API")
public interface AnalyzeApi {
	@Operation(summary = "[프론트에서 호출 XX] 회고 분석 생성", method = "POST", description = """
		마감된 회고에 대한 분석 데이터를 생성합니다.
		""")
	@ApiResponses({
		@ApiResponse(responseCode = "201")
	}
	)
	ResponseEntity<Void> createAnalyze(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @RequestParam List<Long> memberIds);

	@Operation(summary = "회고 분석 조회", method = "GET", description = """
		회고 분석 데이터를 조회합니다.
		""")
	@ApiResponses({
		@ApiResponse(responseCode = "200")
	}
	)
	ResponseEntity<AnalyzesGetResponse> getAnalyze(@PathVariable("spaceId") Long spaceId,
		@PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId);
}
