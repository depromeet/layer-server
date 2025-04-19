package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiMemberExceptionType implements ExceptionType {

    /**
     * 400
     */
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유효한 유저를 찾지 못했습니다."),
    NEED_TO_REGISTER(HttpStatus.BAD_REQUEST, "회원가입이 필요합니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인되지 않은 사용자입니다."),
    NOT_A_NEW_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다.");

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
