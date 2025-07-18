package org.layer.admin.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AdminMemberRole {
	USER,
	ADMIN;

	public static AdminMemberRole fromString(String role) {
		for (AdminMemberRole memberRole : values()) {
			if (memberRole.name().equalsIgnoreCase(role)) {
				return memberRole;
			}
		}
		throw new IllegalArgumentException("Unknown role: " + role);
	}
}
