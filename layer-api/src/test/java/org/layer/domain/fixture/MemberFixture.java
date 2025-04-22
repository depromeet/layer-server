package org.layer.domain.fixture;

import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberRole;
import org.layer.domain.member.entity.SocialType;

public class MemberFixture {
	public static Member createFixture(){
		return Member.builder()
			.name("이름1")
			.email("이메일1")
			.memberRole(MemberRole.USER)
			.socialType(SocialType.KAKAO)
			.socialId("소셜 id1")
			.build();
	}

	public static Member createFixture(String socialId){
		return Member.builder()
			.name("이름1")
			.email("이메일1")
			.memberRole(MemberRole.USER)
			.socialType(SocialType.KAKAO)
			.socialId(socialId)
			.build();
	}
}
