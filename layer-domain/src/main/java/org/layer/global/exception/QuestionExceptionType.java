package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionExceptionType implements ExceptionType {
	NOT_INCLUDED_ESSENTIAL_QUESTION(HttpStatus.BAD_REQUEST, "필수 질문이 포함되어 있지 않습니다."),
	INVALID_QUESTION(HttpStatus.BAD_REQUEST, "유효하지 않은 질문 입니다."),
	INVALID_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 질문 타입입니다."),
	INVALID_QUESTION_SIZE(HttpStatus.BAD_REQUEST, "요청한 질문 응답 수가 유효하지 않습니다.");

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
