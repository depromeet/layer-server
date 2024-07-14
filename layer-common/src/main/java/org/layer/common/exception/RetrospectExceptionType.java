package org.layer.common.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RetrospectExceptionType implements ExceptionType{
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
