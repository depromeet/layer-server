package org.layer.domain.admin.controller.dto;

import java.util.List;

import org.layer.domain.space.dto.AdminSpaceGetResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Schema(name = "AdminSpacesGetResponse", description = "Admin 스페이스 조회")
public class AdminSpacesGetResponse {

	@Schema(description = "스페이스 객체", example = "")
	private final List<AdminSpaceGetResponse> spaces;

	@Schema(description = "총 개수", example = "30")
	private final Integer totalCount;

}
