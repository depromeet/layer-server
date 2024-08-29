package org.layer.domain.analyze.repository;

import static org.layer.common.exception.AnalyzeExcepitonType.*;

import java.util.List;
import java.util.Optional;

import org.layer.domain.analyze.entity.Analyze;
import org.layer.domain.analyze.enums.AnalyzeType;
import org.layer.domain.analyze.exception.AnalyzeExcepiton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnalyzeRepository extends JpaRepository<Analyze, Long> {
	Optional<Analyze> findByRetrospectIdAndAnalyzeType(Long retrospectId, AnalyzeType analyzeType);

	default Analyze findByRetrospectIdAndAnalyzeTypeOrThrow(Long retrospectId, AnalyzeType analyzeType){
		return findByRetrospectIdAndAnalyzeType(retrospectId, analyzeType)
			.orElseThrow(() -> new AnalyzeExcepiton(NOT_FOUND_ANALYZE));
	}

	Optional<Analyze> findByRetrospectIdAndAnalyzeTypeAndMemberId(Long retrospectId, AnalyzeType analyzeType, Long MemberId);

	default Analyze findByRetrospectIdAndAnalyzeTypeAndMemberIdOrThrow(Long retrospectId, AnalyzeType analyzeType, Long memberId){
		return findByRetrospectIdAndAnalyzeTypeAndMemberId(retrospectId, analyzeType, memberId)
			.orElseThrow(() -> new AnalyzeExcepiton(NOT_FOUND_ANALYZE));
	}

	@Query("SELECT a FROM Analyze a "
		+ "JOIN FETCH a.analyzeDetails "
		+ "WHERE a.memberId = :memberId "
		+ "AND a.retrospectId IN :retrospectIds")
	List<Analyze> findAllByMemberIdAndRetrospectIdInQuery(@Param("memberId") Long memberId, @Param("retrospectIds") List<Long> retrospectIds);
}
