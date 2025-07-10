package org.layer.admin.space.listener;

import org.layer.admin.space.service.AdminSpaceService;
import org.layer.event.space.CreateSpaceEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateSpaceEventListener {
	private final AdminSpaceService adminSpaceService;

	@EventListener
	public void handleSignUp(CreateSpaceEvent event) {
		adminSpaceService.saveSpaceHistory(event);
	}
}
