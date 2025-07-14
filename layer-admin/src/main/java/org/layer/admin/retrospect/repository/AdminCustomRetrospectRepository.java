package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminCustomRetrospectRepository {
	List<Long> findConsistentlyMeaningfulMemberIds(
		LocalDateTime startDate, LocalDateTime endDate, int minRetrospectLength, int minRetrospectCount);
}
