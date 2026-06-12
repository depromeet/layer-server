package org.layer.global.exception;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReactionExceptionType implements ExceptionType {
    NOT_FOUND_REACTION(HttpStatus.NOT_FOUND, "존재하지 않는 반응입니다."),
    NOT_FOUND_RETROSPECT_REACTION(HttpStatus.NOT_FOUND, "존재하지 않는 회고 반응입니다."),
    ALREADY_REACTED(HttpStatus.BAD_REQUEST, "이미 반응한 답변입니다."),
    FORBIDDEN_REACTION(HttpStatus.FORBIDDEN, "본인의 반응만 삭제할 수 있습니다.");

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
