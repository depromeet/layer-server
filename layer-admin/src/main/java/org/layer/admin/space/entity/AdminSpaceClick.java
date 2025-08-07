package org.layer.admin.space.entity;

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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminSpaceClick {
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
	@Enumerated(value = EnumType.STRING)
	private AdminRetrospectStatus retrospectStatus;

	@Builder
	private AdminSpaceClick(
		LocalDateTime eventTime,
		Long memberId,
		String eventId,
		Long spaceId,
		AdminRetrospectStatus retrospectStatus
	) {
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
		this.spaceId = spaceId;
		this.retrospectStatus = retrospectStatus;
	}
}
