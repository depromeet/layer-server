package org.layer.admin.retrospect.entity;

import java.time.LocalDateTime;

import org.layer.admin.retrospect.enums.AdminRetrospectStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 특정 회고의 클릭 이벤트를 저장하는 엔티티
 * 이 엔티티는 회고 클릭 이벤트를 기록하여, 나중에 회고의 클릭 이력을 추적할 수 있도록 합니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminRetrospectImpressionClick {
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
	@Enumerated(value = EnumType.STRING)
	private AdminRetrospectStatus retrospectStatus;

	@Builder
	private AdminRetrospectImpressionClick(
		LocalDateTime eventTime,
		Long memberId,
		String eventId,
		Long spaceId,
		Long retrospectId,
		AdminRetrospectStatus retrospectStatus
	) {
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
		this.spaceId = spaceId;
		this.retrospectId = retrospectId;
		this.retrospectStatus = retrospectStatus;
	}
}
