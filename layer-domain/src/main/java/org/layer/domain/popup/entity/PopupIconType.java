package org.layer.domain.popup.entity;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// FE에 사전 정의된 SVG 에셋과 매핑되는 아이콘 식별자. code가 API 계약값이므로 이름을 바꾸지 않는다.
public enum PopupIconType {
    DEFAULT("default"),
    IMPORTANT("important"),
    MAINTENANCE("maintenance"),
    UPDATE("update"),
    EVENT("event"),
    TIP("tip"),
    SECURITY("security");

    private final String code;

    PopupIconType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static PopupIconType from(String code) {
        return Arrays.stream(values())
            .filter(type -> type.code.equalsIgnoreCase(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이콘 id입니다: " + code));
    }
}
