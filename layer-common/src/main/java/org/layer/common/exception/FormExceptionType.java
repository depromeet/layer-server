package org.layer.common.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FormExceptionType implements ExceptionType {

	UNAUTHORIZED_UPDATE_FORM(HttpStatus.UNAUTHORIZED, "회고 폼 수정 권한이 없습니다."),
	NOT_FOUND_FORM(HttpStatus.NOT_FOUND, "찾을 수 없는 회고폼(커스텀 템플릿) 입니다.");

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
