package org.layer.admin.retrospect.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.retrospect.entity.AdminRetrospectHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRetrospectRepository extends JpaRepository<AdminRetrospectHistory, Long> {
	List<AdminRetrospectHistory> findAllByEventTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
