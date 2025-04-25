package org.layer.admin.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(name = "GetMemberActivityResponse", description = "회원 활동 Dto")
public record GetMemberActivityResponse(
	@NotNull
	@Schema(description = "회원 이름", example = "홍길동")
	String name,
	@NotNull
	@Schema(description = "최근 활동 날짜, 최근 6개월 동안 접속 없을 시 null", example = "2024-11-30T16:21:47.031Z")
	LocalDateTime recentActivityDate,
	@NotNull
	@Schema(description = "소속된 스페이스 수", example = "7")
	long spaceCount,
	@NotNull
	@Schema(description = "작성한 회고 수", example = "15")
	long retrospectAnswerCount,
	@NotNull
	@Schema(description = "회원가입 날짜", example = "2024-10-30T16:21:47.031Z")
	LocalDateTime signUpDate,
	@NotNull
	@Schema(description = "회원가입 플랫폼", example = "KAKAO")
	String socialType
) {
}
