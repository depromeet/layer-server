package org.layer.admin.retrospect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AnswerTimeRange {
	MIN_0_TO_5(0, 5, "0-5분"),
	MIN_5_TO_10(5, 10, "5-10분"),
	MIN_10_TO_15(10, 15, "10-15분"),
	MIN_15_TO_20(15, 20, "15-20분"),
	MIN_20_TO_25(20, 25, "20-25분"),
	MIN_25_TO_30(25, 30, "25-30분"),
	MIN_30_PLUS(30, Integer.MAX_VALUE, "30분 이상");

	private final int min;
	private final int max;
	private final String label;

	public static AnswerTimeRange from(long minutes) {
		for (AnswerTimeRange range : values()) {
			if (minutes >= range.min && minutes < range.max) {
				return range;
			}
		}
		throw new IllegalArgumentException("Unknown time range for minutes: " + minutes);
	}
}
