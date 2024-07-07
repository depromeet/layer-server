package org.layer.member.service;

import lombok.RequiredArgsConstructor;
import org.layer.auth.dto.controller.SignUpRequest;
import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.MemberExceptionType;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.layer.common.exception.MemberExceptionType.NOT_A_NEW_MEMBER;
import static org.layer.domain.member.entity.MemberRole.USER;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    // 소셜 아이디와 소셜 타입으로 멤버 찾기. 멤버가 없으면 Exception
    public Member findMemberBySocialIdAndSocialType(String socialId, SocialType socialType) {
        return memberRepository.findBySocialIdAndSocialType(socialId, socialType)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.NOT_FOUND_USER));
    }

    public void checkIsNewMember(String socialId, SocialType socialType) {
        Optional<Member> memberOpt = memberRepository.findBySocialIdAndSocialType(socialId, socialType);

        if(memberOpt.isPresent()) {
            throw new BaseCustomException(NOT_A_NEW_MEMBER);
        }
    }

    public Member saveMember(SignUpRequest signUpRequest, MemberInfoServiceResponse memberInfo) {
        Member member = Member.builder()
                .name(signUpRequest.name())
                .memberRole(USER)
                .email(memberInfo.email())
                .socialId(memberInfo.socialId())
                .socialType(memberInfo.socialType())
                .build();

        memberRepository.save(member);

        return member;
    }

}
