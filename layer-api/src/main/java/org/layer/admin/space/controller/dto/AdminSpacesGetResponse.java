package org.layer.admin.space.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.layer.domain.space.dto.AdminSpaceGetResponse;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Schema(name = "AdminSpacesGetResponse", description = "Admin 스페이스 조회")
public class AdminSpacesGetResponse {

	@Schema(description = "스페이스 객체", example = "")
	private final List<AdminSpaceGetResponse> spaces;

	@Schema(description = "총 개수", example = "30")
	private final Integer totalCount;

}
