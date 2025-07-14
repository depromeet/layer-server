package org.layer.admin.retrospect.repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminCustomRetrospectRepositoryImpl implements AdminCustomRetrospectRepository {

	private final EntityManager em;

	@Override
	public List<Long> findConsistentlyMeaningfulMemberIds(
		LocalDateTime startDate,
		LocalDateTime endDate,
		int minRetrospectLength,
		int minRetrospectCount
	) {
		String sql = """
            SELECT member_id
            FROM (
                SELECT retrospect_id, member_id, SUM(CHAR_LENGTH(answer_content)) AS total_content_length
                FROM layer_prod.answer
                WHERE CHAR_LENGTH(answer_content) >= :minContentLength
                  AND event_time BETWEEN :startDate AND :endDate
                GROUP BY retrospect_id, member_id
            ) AS grouped
            GROUP BY member_id
            HAVING COUNT(*) >= :minRetrospectCount
        """;

		@SuppressWarnings("unchecked")
		List<BigInteger> result = em.createNativeQuery(sql)
			.setParameter("minRetrospectLength", minRetrospectLength)
			.setParameter("minRetrospectCount", minRetrospectCount)
			.setParameter("startDate", startDate)
			.setParameter("endDate", endDate)
			.getResultList();

		return result.stream()
			.map(BigInteger::longValue)
			.toList();
	}
}
