package org.layer.admin.member.listener;

import org.layer.admin.common.UserOnlyEventListener;
import org.layer.admin.member.service.AdminMemberService;
import org.layer.event.member.SignUpEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignUpEventListener {
	private final AdminMemberService adminMemberService;

	@UserOnlyEventListener
	@EventListener
	public void handleSignUp(SignUpEvent event) {
		adminMemberService.saveMemberSignUp(event);
	}
}
