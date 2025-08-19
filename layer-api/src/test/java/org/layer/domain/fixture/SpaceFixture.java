package org.layer.domain.fixture;

import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;

public class SpaceFixture {

	public static Space createFixture(Long leaderId, Long formId){
		return Space.builder()
			.bannerUrl("url1")
			.category(SpaceCategory.TEAM)
			.name("스페이스 이름1")
			.introduction("스페이스 소개1")
			.leaderId(leaderId)
			.formId(formId)
			.build();
	}
}
