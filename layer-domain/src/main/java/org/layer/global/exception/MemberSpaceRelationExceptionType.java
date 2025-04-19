package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberSpaceRelationExceptionType implements ExceptionType {
	/**
	 * 400
	 */
	NOT_FOUND_MEMBER_SPACE_RELATION(HttpStatus.NOT_FOUND, "해당 스페이스에 소속되지 않은 멤버입니다.");

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
