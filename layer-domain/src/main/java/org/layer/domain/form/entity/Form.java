package org.layer.domain.form.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.layer.domain.common.BaseTimeEntity;
import org.layer.domain.form.enums.FormTag;

@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Form extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * 해당 폼 생성한 멤버 id
	 */
	private Long memberId;

	private Long spaceId;

	@NotNull
	private String title;

	@NotNull
	private String introduction;

	@NotNull
	@Enumerated(EnumType.STRING)
	private FormType formType;

	@Enumerated(EnumType.STRING)
	private FormTag formTag; // 기본 템플릿의 명칭. ex) KPT, 5F

	public Form(Long memberId, Long spaceId, String title, String introduction, FormType formType, FormTag formTag) {
		this.memberId = memberId;
		this.spaceId = spaceId;
		this.title = title;
		this.introduction = introduction;
		this.formType = formType;
		this.formTag = formTag;
	}

	public void updateFormTitle(String title) {
		this.title = title;
	}
}
