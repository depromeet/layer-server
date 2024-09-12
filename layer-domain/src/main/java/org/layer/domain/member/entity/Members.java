package org.layer.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Members {
	private final List<Member> members;

	public String getName(Long memberId) {
		return members.stream()
			.filter(member -> member.getId().equals(memberId))
			.map(Member::getName)
			.findAny()
			.orElse(null);
	}

	public String getDelYn(Long memberId) {
		return members.stream()
				.filter(member -> member.getId().equals(memberId))
				.map(Member::getDelYn)
				.findAny()
				.orElse(null);
	}
}
