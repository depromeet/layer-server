package org.layer.admin.template.entity;

import java.time.LocalDateTime;

import org.layer.admin.template.enums.AdminFormTag;
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
 * 특정 유저의 템플릿 선택 이력을 저장하는 엔티티
 * 이 엔티티는 유저가 템플릿을 선택한 시점과 관련된 정보를 기록하여, 나중에 템플릿 선택 이력을 추적할 수 있도록 합니다.
 * 추천을 통해 선택된 템플릿은 AdminChoiceType.RECOMMENDATION으로, 목록에서 선택된 템플릿은 AdminChoiceType.LIST_VIEW로 저장됩니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdminTemplateChoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NotNull
	private AdminFormTag formTag;

	@NotNull
	private LocalDateTime eventTime;

	@NotNull
	private Long memberId;

	@NotNull
	private String eventId;

	@Enumerated(EnumType.STRING)
	@NotNull
	private AdminChoiceType choiceType;

	@Builder
	private AdminTemplateChoice(AdminFormTag formTag, LocalDateTime eventTime, Long memberId, String eventId,
		AdminChoiceType choiceType) {
		this.formTag = formTag;
		this.eventTime = eventTime;
		this.memberId = memberId;
		this.eventId = eventId;
		this.choiceType = choiceType;
	}
}
