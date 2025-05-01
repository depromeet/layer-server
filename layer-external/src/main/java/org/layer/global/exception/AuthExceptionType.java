package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthExceptionType implements ExceptionType {

	/**
	 * 400
	 */
	FAIL_TO_AUTH(HttpStatus.BAD_REQUEST, "인증에 실패했습니다."),
	DUPLICATED_SIGN_UP_REQUEST(HttpStatus.BAD_REQUEST, "중복된 회원 가입 요청입니다. (다른 요청이 이미 처리 중)");

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
