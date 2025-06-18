package org.layer.event.template;

import java.time.LocalDateTime;

public record TemplateRecommendedEvent(
	String eventId,
	Long memberId,
	String formTag,
	LocalDateTime eventTime
) {
}
