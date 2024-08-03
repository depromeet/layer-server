package org.layer.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public enum SpaceExceptionType implements ExceptionType {
    NOT_FOUND_SPACE(HttpStatus.NOT_FOUND, "찾을 수 없는 스페이스 입니다."),
    UNAUTHORIZED_UPDATE_FORM(HttpStatus.UNAUTHORIZED, "회고폼(커스텀 템플릿) 수정 권한이 없습니다."),

    SPACE_LEADER_CANNOT_LEAVE(BAD_REQUEST, "스페이스를 팀장은 떠날 수 없어요."),
    SPACE_ALREADY_JOINED(BAD_REQUEST, "이미 가입한 스페이스에요."),

    CAN_ONLY_SPACE_LEADER(BAD_REQUEST, "스페이스 대표자만 가능해요."),

    SPACE_LEADER_NOT_ALLOW(BAD_REQUEST, "이미 스페이스 대표자에요.");


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
