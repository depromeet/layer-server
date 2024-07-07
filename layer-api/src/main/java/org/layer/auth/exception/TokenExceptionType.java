package org.layer.auth.exception;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TokenExceptionType implements ExceptionType {

    /**
     * 400
     */
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "token이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh token이 유효하지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "access token이 유효하지 않습니다.");


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
