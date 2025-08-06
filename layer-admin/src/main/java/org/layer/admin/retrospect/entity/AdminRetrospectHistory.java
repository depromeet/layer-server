package org.layer.admin.retrospect.entity;

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

/*
 * 특정 회고의 생성 시점을 저장하는 엔티티
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminRetrospectHistory {
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

	@NotNull
	private long targetAnswerCount;

	@Builder
	private AdminRetrospectHistory(LocalDateTime eventTime, Long memberId, String eventId, Long spaceId,
		Long retrospectId, long targetAnswerCount) {
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
		this.spaceId = spaceId;
		this.retrospectId = retrospectId;
		this.targetAnswerCount = targetAnswerCount;
	}
}
