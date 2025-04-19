package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiFormExceptionType implements ExceptionType {
	UNAUTHORIZED_GET_FORM(HttpStatus.UNAUTHORIZED, "회고 폼 조회 권한이 없습니다.");

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
