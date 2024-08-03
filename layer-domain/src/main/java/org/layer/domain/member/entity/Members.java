package org.layer.domain.member.entity;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
}
