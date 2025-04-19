package org.layer.domain.fixture;

import org.layer.domain.form.entity.Form;
import org.layer.domain.form.entity.FormType;
import org.layer.domain.form.enums.FormTag;

public class FormFixture {

	public static Form createFixture(Long memberId, Long spaceId){
		return Form.builder()
			.memberId(memberId)
			.spaceId(spaceId)
			.title("폼 제목1")
			.introduction("폼 소개1")
			.formType(FormType.CUSTOM)
			.formTag(FormTag.KPT)
			.build();
	}
}
