package org.layer.admin.template.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AdminFormTag {
	KPT("KPT"),
	FIVE_F("5F"),
	MAD_SAD_GLAD("Mad Sad Glad"),
	SSC("SSC"),
	PMI("PMI"),
	UNTITLED("무제"),
	CUSTOM("CUSTOM");

	private final String tag;

	public static AdminFormTag from(String tag) {
		for (AdminFormTag formTag : values()) {
			if (formTag.getTag().equalsIgnoreCase(tag)) {
				return formTag;
			}
		}
		throw new IllegalArgumentException("Unknown tag: " + tag);
	}
}
