package org.layer.admin.space.listener;

import org.layer.admin.space.service.AdminSpaceService;
import org.layer.event.space.CreateSpaceEvent;
import org.layer.event.space.JoinSpaceEvent;
import org.layer.event.space.LeaveSpaceEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpaceEventListener {
	private final AdminSpaceService adminSpaceService;

	@EventListener
	public void handleSignUp(CreateSpaceEvent event) {
		adminSpaceService.saveSpaceHistory(event);
	}

	@EventListener
	public void handleJoinSpace(JoinSpaceEvent event) {
		adminSpaceService.saveMemberSpaceHistory(event);
	}

	@EventListener
	public void handleLeaveSpace(LeaveSpaceEvent event) {
		adminSpaceService.deleteMemberSpaceHistory(event);
	}
}
