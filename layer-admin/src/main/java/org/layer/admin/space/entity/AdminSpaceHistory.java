package org.layer.admin.space.entity;

import java.time.LocalDateTime;

import org.layer.admin.space.enums.AdminSpaceCategory;

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
public class AdminSpaceHistory {
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
	@Enumerated(EnumType.STRING)
	private AdminSpaceCategory category;

	@Builder
	private AdminSpaceHistory(
		LocalDateTime eventTime,
		Long memberId,
		String eventId,
		Long spaceId,
		AdminSpaceCategory category
	) {
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
		this.spaceId = spaceId;
		this.category = category;
	}
}
