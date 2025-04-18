package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    /**
     * 400
     */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유효한 유저를 찾지 못했습니다.");

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
