package org.layer.admin.template.entity;

import java.time.LocalDateTime;

import org.layer.admin.template.enums.AdminChoiceType;

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
 * 특정 유저의 템플릿 클릭 이력을 저장하는 엔티티
 * 이 엔티티는 유저가 [추천받기] 또는 [리스트 보기] 버튼을 클릭한 시점을 기록합니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminTemplateClickHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NotNull
	private AdminChoiceType viewType;

	@NotNull
	private LocalDateTime eventTime;

	@NotNull
	private Long memberId;

	@NotNull
	private String eventId;

	@Builder
	private AdminTemplateClickHistory(AdminChoiceType viewType, LocalDateTime eventTime, Long memberId, String eventId) {
		this.viewType = viewType;
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
	}
}
