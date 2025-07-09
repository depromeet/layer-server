package org.layer.event.template;

import java.time.LocalDateTime;

public record TemplateListViewEvent(
	String eventId,
	Long memberId,
	LocalDateTime eventTime
) {
}
