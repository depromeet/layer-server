package org.layer.admin.retrospect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WritingCycleRange {
    PERIODIC("주기적 (7일↓)", 0, 7),
    BIWEEKLY("격주~월 (8~30일)", 8, 30),
    QUARTERLY("분기적 (31~90일)", 31, 90),
    IRREGULAR("비정기 (91~180일)", 91, 180),
    DORMANT("휴면 (181일+)", 181, Integer.MAX_VALUE);

    private final String label;
    private final int minDays;
    private final int maxDays;

    public static WritingCycleRange from(double days) {
        for (WritingCycleRange range : values()) {
            if (days >= range.minDays && days <= range.maxDays) return range;
        }
        return DORMANT;
    }
}
