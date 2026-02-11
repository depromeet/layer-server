package org.layer.domain.space.entity;

import static org.layer.global.exception.MemberSpaceRelationExceptionType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.domain.space.exception.MemberSpaceRelationException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Team {
	private final List<MemberSpaceRelation> memberSpaceRelations;

	public void validateTeamMembership(Long memberId){
		memberSpaceRelations.stream()
			.filter(memberSpaceRelation -> memberSpaceRelation.getMemberId().equals(memberId))
			.findAny()
			.orElseThrow(() -> new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION));
	}

	public long getTeamMemberCount(){
		return memberSpaceRelations.size();
	}

	public List<Long> getMemberIds(){
		return memberSpaceRelations.stream()
			.map(MemberSpaceRelation::getMemberId)
			.toList();
	}

	public long getTeamMemberCountBefore(LocalDateTime end) {
		return memberSpaceRelations.stream()
			.filter(memberSpaceRelation -> memberSpaceRelation.getCreatedAt().isBefore(end))
			.count();
	}
}
