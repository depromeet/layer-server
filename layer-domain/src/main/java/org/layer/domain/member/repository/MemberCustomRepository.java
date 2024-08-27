package org.layer.domain.member.repository;

import org.layer.domain.member.entity.MemberFeedback;

import java.util.Optional;


public interface MemberCustomRepository {

    Optional<MemberFeedback> findAllMemberFeedback(Long memberId);
}
