package org.layer.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AdminExceptionType implements ExceptionType{
    IllegalDateTime(HttpStatus.INTERNAL_SERVER_ERROR, "유저의 최근 접속 시간이 올바르지 않습니다.");


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