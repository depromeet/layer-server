package org.layer.common.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerExceptionType implements ExceptionType{
	ALREADY_ANSWERED(HttpStatus.BAD_REQUEST, "이미 응답한 질문이 있습니다."),
	NOT_ANSWERED(HttpStatus.BAD_REQUEST, "이전에 응답한 질문이 없습니다.");

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
