package org.layer.domain.retrospect.entity;

import static org.layer.common.exception.RetrospectExceptionType.*;

import java.time.LocalDateTime;

import org.layer.domain.common.BaseTimeEntity;
import org.layer.domain.common.time.Time;
import org.layer.domain.retrospect.exception.RetrospectException;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Retrospect extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Long spaceId;

	@NotNull
	private String title;

	@NotNull
	private String introduction;

	@NotNull
	@Enumerated(EnumType.STRING)
	private RetrospectStatus retrospectStatus;

	@NotNull
	@Enumerated(EnumType.STRING)
	private AnalysisStatus analysisStatus;

	private LocalDateTime deadline;

	@Builder
	public Retrospect(Long spaceId, String title, String introduction, RetrospectStatus retrospectStatus,
		AnalysisStatus analysisStatus, LocalDateTime deadline) {
		this.spaceId = spaceId;
		this.title = title;
		this.introduction = introduction;
		this.retrospectStatus = retrospectStatus;
		this.analysisStatus = analysisStatus;
		this.deadline = deadline;
	}

	public void isProceedingRetrospect() {
		if (!this.retrospectStatus.equals(RetrospectStatus.PROCEEDING)) {
			throw new RetrospectException(NOT_PROCEEDING_RETROSPECT);
		}
	}

	public void validateDeadline(LocalDateTime currentTime) {
		if (this.deadline != null && currentTime.isAfter(this.deadline)) {
			throw new RetrospectException(DEADLINE_PASSED);
		}
	}

	public void validateAnalysisStatusIsNotDone() {
		if (this.analysisStatus.equals(AnalysisStatus.DONE)) {
			throw new RetrospectException(ALREADY_ANALYSIS_DONE);
		}
	}

	public void updateRetrospect(String title, String introduction, LocalDateTime deadline, Time time) {

		if (deadline != null && deadline.isBefore(time.now())) {
			throw new RetrospectException(INVALID_DEADLINE);
		}

		this.title = title;
		this.introduction = introduction;
		this.deadline = deadline;
	}

	public void updateRetrospectStatus(RetrospectStatus retrospectStatus) {
		isProceedingRetrospect();

		this.retrospectStatus = retrospectStatus;
	}

	public void updateAnalysisStatus(AnalysisStatus analysisStatus) {
		this.analysisStatus = analysisStatus;
	}

	public boolean hasDeadLine() {
		if (this.deadline == null) {
			return false;
		}

		return true;
	}

	public void updateDeadLine(LocalDateTime deadline){
		this.deadline = deadline;
	}
}
