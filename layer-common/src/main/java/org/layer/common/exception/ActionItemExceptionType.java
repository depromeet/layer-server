package org.layer.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ActionItemExceptionType implements ExceptionType{
    NOT_FOUND_ACTION_ITEM(HttpStatus.NOT_FOUND, "액션 아이템이 존재하지 않습니다."),
    CANNOT_DELETE_ACTION_ITEM(HttpStatus.FORBIDDEN, "액션 아이템은 작성한 사람만 지울 수 있습니다.");


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