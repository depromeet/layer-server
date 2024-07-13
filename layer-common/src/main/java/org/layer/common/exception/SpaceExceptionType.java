package org.layer.common.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpaceExceptionType implements ExceptionType{
	NOT_FOUND_SPACE(HttpStatus.NOT_FOUND, "유효하지 않은 스페이스 id 입니다.");

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
