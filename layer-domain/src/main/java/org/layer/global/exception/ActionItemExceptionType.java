package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ActionItemExceptionType implements ExceptionType {
    NOT_FOUND_ACTION_ITEM(HttpStatus.NOT_FOUND, "실행 목표가 존재하지 않습니다.");

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