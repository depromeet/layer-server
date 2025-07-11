package org.layer.discord.notifier;

import java.util.Map;

public record MemberActivityEvent(
	Map<Long, Map<String, Integer>> activities
) {
	public static MemberActivityEvent of(Map<Long, Map<String, Integer>> activities){
		return new MemberActivityEvent(activities);
	}
}
