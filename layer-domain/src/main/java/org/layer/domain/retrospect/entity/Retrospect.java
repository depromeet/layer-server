package org.layer.domain.retrospect.entity;

import static org.layer.common.exception.RetrospectExceptionType.*;

import java.time.LocalDateTime;

import org.layer.domain.common.BaseTimeEntity;
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
	private LocalDateTime deadline;

	@Builder
	public Retrospect(Long spaceId, String title, String introduction, RetrospectStatus retrospectStatus,
		LocalDateTime deadline) {
		this.spaceId = spaceId;
		this.title = title;
		this.introduction = introduction;
		this.retrospectStatus = retrospectStatus;
		this.deadline = deadline;
	}

	public void isProceedingRetrospect() {
		if (!this.retrospectStatus.equals(RetrospectStatus.PROCEEDING)) {
			throw new RetrospectException(NOT_PROCEEDING_RETROSPECT);
		}
	}
}
