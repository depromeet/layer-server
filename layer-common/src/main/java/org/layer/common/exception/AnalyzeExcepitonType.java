package org.layer.common.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnalyzeExcepitonType implements ExceptionType {
	NOT_FOUND_ANALYZE(HttpStatus.NOT_FOUND, "분석 정보가 존재하지 않습니다.");


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
