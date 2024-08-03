package org.layer.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.MemberExceptionType;
import org.layer.domain.auth.controller.dto.SignUpRequest;
import org.layer.domain.jwt.SecurityUtil;
import org.layer.domain.member.controller.dto.UpdateMemberInfoRequest;
import org.layer.domain.member.controller.dto.UpdateMemberInfoResponse;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.layer.common.exception.MemberExceptionType.NOT_A_NEW_MEMBER;
import static org.layer.common.exception.MemberExceptionType.NOT_FOUND_USER;
import static org.layer.domain.member.entity.MemberRole.USER;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    // 소셜 아이디와 소셜 타입으로 멤버 찾기. 멤버가 없으면 Exception
    // 현재는 사용하지 않음
    public Member getMemberBySocialIdAndSocialType(String socialId, SocialType socialType) {
        return memberRepository.findBySocialIdAndSocialType(socialId, socialType)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.NOT_FOUND_USER));
    }

    // sign-in만을 위한 메서드. 멤버가 없을시 회원가입이 필요함을 알려준다.
    // 회원이 진짜로 없는 error의 경우와 회원 가입이 필요하다는 응답을 구분하기 위함
    public Member getMemberBySocialInfoForSignIn(String socialId, SocialType socialType) {
        return memberRepository.findBySocialIdAndSocialType(socialId, socialType)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.NEED_TO_REGISTER));
    }

    public void checkIsNewMember(String socialId, SocialType socialType) {
        Optional<Member> memberOpt = memberRepository.findBySocialIdAndSocialType(socialId, socialType);

        if(memberOpt.isPresent()) {
            throw new BaseCustomException(NOT_A_NEW_MEMBER);
        }
    }

    @Transactional
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

    @Transactional
    public void withdrawMember(Long memberId) {
        Member currentMember = getCurrentMember();
        memberRepository.delete(currentMember);
    }


    @Transactional
    public UpdateMemberInfoResponse updateMemberInfo(Long memberId, UpdateMemberInfoRequest updateMemberInfoRequest) {
        Member member = memberRepository.findByIdOrThrow(memberId);
        member.updateName(updateMemberInfoRequest.name());
        member.updateProfileImageUrl(updateMemberInfoRequest.profileImageUrl());


        return UpdateMemberInfoResponse.builder()
                .name(member.getName())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
