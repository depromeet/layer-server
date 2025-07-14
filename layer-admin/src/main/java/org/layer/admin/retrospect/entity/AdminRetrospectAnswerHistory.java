package org.layer.admin.retrospect.entity;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminRetrospectAnswerHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private LocalDateTime eventTime;

	@NotNull
	private Long memberId;

	@NotNull
	private String eventId;

	@NotNull
	private Long spaceId;

	@NotNull
	private Long retrospectId;

	private LocalDateTime answerStartTime;

	private LocalDateTime answerEndTime;

	private String answerContent;

	public void updateRetrospectCompleted (LocalDateTime answerEndTime, String answerContent) {
		this.answerEndTime = answerEndTime;
		this.answerContent = answerContent;
	}

	public long getAnswerTime() {
		if (answerStartTime == null || answerEndTime == null) {
			return 0L;
		}
		Duration duration = Duration.between(answerStartTime, answerEndTime);

		return duration.toMinutes();
	}

	@Builder
	private AdminRetrospectAnswerHistory(LocalDateTime eventTime, Long memberId, String eventId, Long spaceId,
		Long retrospectId, LocalDateTime answerStartTime, LocalDateTime answerEndTime, String answerContent) {
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
		this.spaceId = spaceId;
		this.retrospectId = retrospectId;
		this.answerStartTime = answerStartTime;
		this.answerEndTime = answerEndTime;
		this.answerContent = answerContent;
	}
}
