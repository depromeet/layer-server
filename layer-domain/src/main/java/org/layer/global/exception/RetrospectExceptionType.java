package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RetrospectExceptionType implements ExceptionType {
	INVALID_DEADLINE(HttpStatus.BAD_REQUEST, "회고 마감기한이 유효하지 않습니다."),
	DEADLINE_PASSED(HttpStatus.BAD_REQUEST, "회고 마감기한이 지났습니다."),
	ALREADY_ANALYSIS_DONE(HttpStatus.BAD_REQUEST, "회고 분석을 이미 마쳤습니다."),
	NOT_PROCEEDING_RETROSPECT(HttpStatus.BAD_REQUEST, "진행중인 회고가 아닙니다."),
	NOT_FOUND_RETROSPECT(HttpStatus.NOT_FOUND, "유효한 회고가 존재하지 않습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public HttpStatus httpStatus() {
		return status;
	}

	@Override
	public String message() {
		return message;
	}
}
