package org.layer.admin.retrospect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RetrospectCycleRange {
	DAY_1_TO_3("1-3일"),
	DAY_4_TO_7("4-7일"),
	DAY_8_TO_14("8-14일"),
	DAY_15_TO_30("15-30일"),
	DAY_31_PLUS("31일+");

	private final String label;

	public static RetrospectCycleRange from(double days) {
		if (days <= 3) return DAY_1_TO_3;
		if (days <= 7) return DAY_4_TO_7;
		if (days <= 14) return DAY_8_TO_14;
		if (days <= 30) return DAY_15_TO_30;
		return DAY_31_PLUS;
	}
}
