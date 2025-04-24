package org.layer.domain.admin.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MemberActivityCache {
	public final Map<Long, Map<String, Integer>> activityMap = new ConcurrentHashMap<>();

}
