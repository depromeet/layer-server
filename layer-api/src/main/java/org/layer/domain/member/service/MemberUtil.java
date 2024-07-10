package org.layer.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.jwt.SecurityUtil;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

import static org.layer.common.exception.MemberExceptionType.NOT_FOUND_USER;
@RequiredArgsConstructor
@Component
public class MemberUtil {
    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    public Member getCurrentMember() {
        return memberRepository
                .findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new BaseCustomException(NOT_FOUND_USER));
    }

    public Member getMemberByMemberId(Long memberId) {
        return memberRepository.
                findById(memberId)
                .orElseThrow(() -> new BaseCustomException(NOT_FOUND_USER));
    }
}
