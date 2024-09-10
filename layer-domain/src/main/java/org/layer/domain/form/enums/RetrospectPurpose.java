package org.layer.domain.form.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RetrospectPurpose {
	CHECK_PROGRESS(1, 0, 0, 1, 0),
	PERSONAL_GROWTH(1, 0, 0, 0, 0),
	TEAM_GROWTH(1, 0, 1, 1, 1),
	IMPROVE_COMMUNICATION(0, 0, 1, 0, 1),
	SHARE_EXPERIENCE(0, 1, 0, 0, 1),
	IMPROVE_PROBLEM(1, 1, 0, 0, 0),
	SHARE_EMOTION(0, 1, 1, 0, 0),
	STRATEGY_SETTING(1, 0, 0, 1, 0);

	private final int ktpPoint;
	private final int fiveFPoint;
	private final int madSadGladPoint;
	private final int sscPoint;
	private final int pmiPont;

}
