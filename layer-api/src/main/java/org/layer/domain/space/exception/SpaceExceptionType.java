package org.layer.domain.space.exception;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum SpaceExceptionType implements ExceptionType {


    SPACE_NOT_FOUND(BAD_REQUEST, "스페이스를 찾을 수 없어요."),

    SPACE_NOT_JOINED(BAD_REQUEST, "참여중인 스페이스가 아니에요."),
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
