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
