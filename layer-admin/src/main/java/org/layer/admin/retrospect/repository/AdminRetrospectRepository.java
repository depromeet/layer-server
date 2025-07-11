package org.layer.admin.retrospect.repository;


import java.util.Optional;

import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AdminRetrospectRepository extends JpaRepository<AdminRetrospectAnswerHistory, Long> {

	Optional<AdminRetrospectAnswerHistory> findByMemberIdAndSpaceIdAndRetrospectId(
		@Param("memberId") Long memberId,
		@Param("spaceId") Long spaceId,
		@Param("retrospectId") Long retrospectId
	);
}
