package org.layer.domain.space.dto;


import org.layer.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record Leader(
        Long id,
        String name
) {
	public static Leader of(Member member){
		return new Leader(member.getId(), member.getName());
	}
}
