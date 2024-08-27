package org.layer.common.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenExceptionType implements ExceptionType {
	/**
	 * 400
	 */
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "token이 유효하지 않습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh token이 유효하지 않습니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "access token이 유효하지 않습니다."),
	NO_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh token이 존재하지 않습니다."),
	INVALID_APPLE_ID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 apple token입니다.");


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
