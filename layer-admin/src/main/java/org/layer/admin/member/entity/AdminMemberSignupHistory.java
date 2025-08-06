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

/**
 * 특정 유저의 회원 가입 시점을 저장하는 엔티티
 * 이 엔티티는 유저의 회원 가입 이벤트를 기록하여, 나중에 회원 가입 이력을 추적할 수 있도록 합니다.
 */
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
