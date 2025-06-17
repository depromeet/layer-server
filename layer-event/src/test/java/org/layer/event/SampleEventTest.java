package org.layer.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SampleEventTest {

    @Test
    void factoryMethodCreatesEvent() {
        SampleEvent event = SampleEvent.of("hello");
        assertEquals("hello", event.message());
    }
}
