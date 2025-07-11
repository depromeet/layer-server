package org.layer.admin.retrospect.listener;

import org.layer.admin.retrospect.service.AdminRetrospectService;
import org.layer.event.retrospect.WriteRetrospectEndEvent;
import org.layer.event.retrospect.WriteRetrospectStartEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WriteRetrospectEventListener {
	private final AdminRetrospectService adminRetrospectService;

	@EventListener
	public void handleWriteRetrospectStart(WriteRetrospectStartEvent event) {
		adminRetrospectService.saveRetrospectAnswerHistory(event);
	}

	@EventListener
	public void handleWriteRetrospectEnd(WriteRetrospectEndEvent event) {
		adminRetrospectService.updateRetrospectAnswerHistory(event);
	}
}
