package org.layer.admin.retrospect.listener;

import org.layer.admin.common.UserOnlyEventListener;
import org.layer.admin.retrospect.service.AdminRetrospectService;
import org.layer.event.retrospect.CreateRetrospectEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateRetrospectEventListener {
	private final AdminRetrospectService adminRetrospectService;

	@UserOnlyEventListener
	@EventListener
	public void handleWriteRetrospectStart(CreateRetrospectEvent event) {
		adminRetrospectService.saveRetrospectHistory(event);
	}
}
