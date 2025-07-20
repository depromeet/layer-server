package org.layer.batch.retrospect;

import java.time.LocalDateTime;

import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;

public class RetrospectFixture {
	public static Retrospect createFixture(Long spaceId, RetrospectStatus retrospectStatus,
		AnalysisStatus analysisStatus, LocalDateTime deadline) {
		return Retrospect.builder()
			.spaceId(spaceId)
			.title("회고제목1")
			.introduction("회고소개1")
			.retrospectStatus(retrospectStatus)
			.analysisStatus(analysisStatus)
			.deadline(deadline)
			.build();
	}
}
