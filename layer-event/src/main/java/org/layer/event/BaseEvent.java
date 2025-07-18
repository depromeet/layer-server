package org.layer.event;

import java.time.LocalDateTime;

public interface BaseEvent {
    String eventId();
    Long memberId();
    LocalDateTime eventTime();
}
