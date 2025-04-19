package org.layer.domain.fixture;

import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;

public class MemberSpaceRelationFixture {

	public static MemberSpaceRelation createFixture(Space space, Long memberId){
		return MemberSpaceRelation.builder()
			.space(space)
			.memberId(memberId)
			.build();
	}
}
