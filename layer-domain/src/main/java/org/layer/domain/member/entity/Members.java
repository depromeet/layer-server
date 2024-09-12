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

	public Boolean getDeleted(Long memberId) {
		return members.stream()
				.filter(member -> member.getId().equals(memberId))
				.map(m -> m.getDeletedAt() == null) // 지워졌으면 true, 아니면 false
				.findAny()
				.orElse(null);
	}
}
