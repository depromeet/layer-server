package org.layer.admin.space.listener;

import org.layer.admin.common.UserOnlyEventListener;
import org.layer.admin.space.service.AdminSpaceService;
import org.layer.event.space.ClickSpaceEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpaceClickEventListener {
	private final AdminSpaceService adminSpaceService;

	@UserOnlyEventListener
	@EventListener
	public void handleClickSpaceEvent(ClickSpaceEvent event) {
		adminSpaceService.saveSpaceImpressionClick(event);
	}
}
