package org.layer.admin.retrospect.enums;

public enum AdminRetrospectStatus {
	PROCEEDING, DONE;

	public static AdminRetrospectStatus from(String status) {
		return AdminRetrospectStatus.valueOf(status.toUpperCase());
	}
}
