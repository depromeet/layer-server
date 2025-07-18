package org.layer.admin.retrospect.listener;

import org.layer.admin.common.UserOnlyEventListener;
import org.layer.admin.retrospect.service.AdminRetrospectService;
import org.layer.event.retrospect.AnswerRetrospectEndEvent;
import org.layer.event.retrospect.AnswerRetrospectStartEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnswerRetrospectEventListener {
	private final AdminRetrospectService adminRetrospectService;

	@UserOnlyEventListener
	@EventListener
	public void handleWriteRetrospectStart(AnswerRetrospectStartEvent event) {
		adminRetrospectService.saveRetrospectAnswerHistory(event);
	}

	@UserOnlyEventListener
	@EventListener
	public void handleWriteRetrospectEnd(AnswerRetrospectEndEvent event) {
		adminRetrospectService.updateRetrospectAnswerHistory(event);
	}
}
