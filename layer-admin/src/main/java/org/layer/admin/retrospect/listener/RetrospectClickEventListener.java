package org.layer.admin.retrospect.listener;

import org.layer.admin.common.UserOnlyEventListener;
import org.layer.admin.retrospect.service.AdminRetrospectService;
import org.layer.event.retrospect.ClickRetrospectEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RetrospectClickEventListener {
	private final AdminRetrospectService adminRetrospectService;

	@UserOnlyEventListener
	@EventListener
	public void handleClickRetrospectEvent(ClickRetrospectEvent event) {
		adminRetrospectService.saveRetrospectImpressionClick(event);
	}
}
