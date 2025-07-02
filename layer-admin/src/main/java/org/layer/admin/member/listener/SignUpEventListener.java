package org.layer.admin.member.listener;

import org.layer.admin.member.service.AdminMemberService;
import org.layer.event.member.SignUpEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignUpEventListener {
	private final AdminMemberService adminMemberService;

	@EventListener
	public void handleSignUp(SignUpEvent event) {
		adminMemberService.saveMemberSignUp(event);
	}
}
