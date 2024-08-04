package org.layer.domain.actionItem.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionItemStatus {
    BEFORE_START(1),
    PROCEEDING(0),
    DONE(2);

    private final Integer priority;
}
