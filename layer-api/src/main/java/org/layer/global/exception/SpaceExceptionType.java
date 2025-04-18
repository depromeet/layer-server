package org.layer.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpaceExceptionType implements ExceptionType {
    NOT_FOUND_SPACE(HttpStatus.NOT_FOUND, "찾을 수 없는 스페이스 입니다."),
    SPACE_LEADER_CANNOT_LEAVE(BAD_REQUEST, "스페이스를 팀장은 떠날 수 없어요."),
    SPACE_ALREADY_JOINED(BAD_REQUEST, "이미 가입한 스페이스에요.");


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
