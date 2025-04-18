package org.layer.global.exception;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerExceptionType implements ExceptionType {
    ALREADY_ANSWERED(HttpStatus.BAD_REQUEST, "이미 응답한 질문이 있습니다."),
    NOT_ANSWERED(HttpStatus.BAD_REQUEST, "이전에 응답한 질문이 없습니다."),
    NOT_CONTAIN_ANSWERS(HttpStatus.BAD_REQUEST, "올바르지 않은 답변입니다.");

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
