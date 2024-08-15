package org.layer.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ActionItemExceptionType implements ExceptionType{
    NOT_FOUND_ACTION_ITEM(HttpStatus.NOT_FOUND, "실행 목표가 존재하지 않습니다."),
    INVALID_ACTION_ITEM_ID(HttpStatus.BAD_REQUEST, "잘못된 실행 목표 id입니다."),
    INVALID_ACTION_ITEM_LIST(HttpStatus.BAD_REQUEST, "잘못된 실행 목표 리스트입니다.");


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