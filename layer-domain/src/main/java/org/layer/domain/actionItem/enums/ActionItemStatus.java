package org.layer.domain.actionItem.enums;

public enum ActionItemStatus {
    PROCESSING,
    RESOLVED; // MVP 단계에선 생성, 삭제 밖에 없는 것 같지만, 확장성 고려
}
