package org.layer.admin.space.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.space.controller.dto.TeamSpaceRatioPerMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminSpaceRepositoryImpl implements AdminSpaceRepositoryCustom {

	private final EntityManager em;

	@Override
	public Page<TeamSpaceRatioPerMemberDto> findTeamSpaceRatioPerMemberWithPeriod(LocalDateTime start, LocalDateTime end, Pageable pageable) {
		String sql = """
            SELECT ash.member_id AS memberId,
                   COUNT(*) AS totalCount,
                   SUM(CASE WHEN ash.category = 'TEAM' THEN 1 ELSE 0 END) AS teamCount
            FROM admin_space_history ash
            WHERE ash.event_time BETWEEN :startTime AND :endTime
            GROUP BY ash.member_id
            ORDER BY ash.member_id
            LIMIT :limit OFFSET :offset
        """;

		List<Object[]> rows = em.createNativeQuery(sql)
			.setParameter("startTime", start)
			.setParameter("endTime", end)
			.setParameter("limit", pageable.getPageSize())
			.setParameter("offset", pageable.getOffset())
			.getResultList();

		List<TeamSpaceRatioPerMemberDto> content = rows.stream()
			.map(row -> new TeamSpaceRatioPerMemberDto(
				((Number) row[0]).longValue(),
				((Number) row[1]).longValue(),
				((Number) row[2]).longValue()
			))
			.toList();

		// 전체 멤버 수 (조건 포함)
		Long total = ((Number) em.createNativeQuery("""
            SELECT COUNT(DISTINCT ash.member_id)
            FROM admin_space_history ash
            WHERE ash.event_time BETWEEN :startTime AND :endTime
        """)
			.setParameter("startTime", start)
			.setParameter("endTime", end)
			.getSingleResult()).longValue();

		return new PageImpl<>(content, pageable, total);
	}

	public double findAverageOfTeamSpaceRatiosWithPeriod(LocalDateTime startTime, LocalDateTime endTime) {
		String sql = """
        SELECT AVG(ratio) FROM (
            SELECT
                member_id,
                SUM(CASE WHEN category = 'TEAM' THEN 1 ELSE 0 END) * 1.0 / COUNT(*) AS ratio
            FROM admin_space_history
            WHERE event_time BETWEEN :startTime AND :endTime
            GROUP BY member_id
        ) AS per_member
    """;

		Object result = em.createNativeQuery(sql)
			.setParameter("startTime", startTime)
			.setParameter("endTime", endTime)
			.getSingleResult();

		if (result == null) return 0.0;
		return ((Number) result).doubleValue();
	}

}
