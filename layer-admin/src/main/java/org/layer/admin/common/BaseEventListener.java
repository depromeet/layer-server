package org.layer.admin.common;

import org.layer.admin.member.entity.AdminMemberRole;
import org.layer.admin.member.entity.AdminMemberSignupHistory;
import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.event.BaseEvent;

public abstract class BaseEventListener<T extends BaseEvent> {
	private final AdminMemberRepository adminMemberRepository;

	protected BaseEventListener(AdminMemberRepository adminMemberRepository) {
		this.adminMemberRepository = adminMemberRepository;
	}

	public final void handle(T event) {
		AdminMemberSignupHistory member = adminMemberRepository.findById(event.memberId())
			.orElseThrow(() -> new IllegalArgumentException("Member not found"));

		if (member.getMemberRole() == AdminMemberRole.ADMIN) {
			return;
		}

		handleEvent(event);
	}

	protected abstract void handleEvent(T event);
}
