package org.layer.admin.member.entity;

import java.time.LocalDateTime;

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
public class AdminMemberSignupHistory {
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
	@Enumerated(EnumType.STRING)
	private AdminMemberRole memberRole;

	@Builder
	private AdminMemberSignupHistory(
		LocalDateTime eventTime,
		Long memberId,
		String eventId,
		AdminMemberRole memberRole
	) {
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
		this.memberRole = memberRole;
	}
}
