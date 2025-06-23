package org.layer.admin.member.controller.dto;

import java.time.LocalDate;

public record MemberSignupCountResponse(
	LocalDate signupDate,
	long signupCount
) {
}
