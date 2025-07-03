package org.layer.admin.template.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdminViewType {
	RECOMMENDATION("RECOMMENDATION"),
	LIST_VIEW("LIST_VIEW");

	private final String type;

}
