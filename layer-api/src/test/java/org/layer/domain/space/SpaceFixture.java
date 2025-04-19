package org.layer.domain.space;

import java.util.List;

import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;

public class SpaceFixture {

	public static Space createSpaceFixture(Long leaderId){
		return Space.builder()
			.bannerUrl("url1")
			.category(SpaceCategory.TEAM)
			.fieldList(List.of(SpaceField.DESIGN, SpaceField.DEVELOPMENT))
			.name("스페이스 이름1")
			.introduction("스페이스 소개1")
			.leaderId(leaderId)
			.formId(10001L)
			.build();
	}
}
