package org.layer.event;

/**
 * Simple event used for testing the layer-event module.
 */
public record SampleEvent(String message) {
    public static SampleEvent of(String message) {
        return new SampleEvent(message);
    }
}
