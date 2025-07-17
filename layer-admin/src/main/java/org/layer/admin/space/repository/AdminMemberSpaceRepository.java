package org.layer.admin.space.repository;

import org.layer.admin.space.entity.AdminMemberSpaceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberSpaceRepository extends JpaRepository<AdminMemberSpaceHistory, Long>, AdminMemberSpaceRepositoryCustom {
	void deleteByMemberIdAndSpaceId(Long memberId, Long spaceId);
}
