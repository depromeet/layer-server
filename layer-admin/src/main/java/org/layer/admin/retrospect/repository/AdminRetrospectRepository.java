package org.layer.admin.retrospect.repository;

import org.layer.admin.retrospect.entity.AdminRetrospectHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRetrospectRepository extends JpaRepository<AdminRetrospectHistory, Long> {

}
