package org.layer.domain.jwt.exception;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthExceptionType implements ExceptionType {

    /**
     * 400
     */
    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 방식의 로그인입니다."),
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
