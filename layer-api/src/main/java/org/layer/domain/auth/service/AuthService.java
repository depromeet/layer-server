package org.layer.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.auth.controller.dto.SignUpRequest;
import org.layer.domain.auth.service.dto.ReissueTokenServiceResponse;
import org.layer.domain.auth.controller.dto.SignInResponse;
import org.layer.domain.auth.service.dto.SignUpServiceResponse;
import org.layer.domain.jwt.JwtToken;
import org.layer.domain.jwt.exception.AuthExceptionType;
import org.layer.domain.jwt.service.JwtService;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.service.MemberService;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.service.GoogleService;
import org.layer.oauth.service.KakaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtService jwtService;
    private final MemberService memberService;
    //== 로그인 ==//
    @Transactional
    public SignInResponse signIn(final String socialAccessToken, final SocialType socialType) {
        MemberInfoServiceResponse signedMember = getMemberInfo(socialType, socialAccessToken);

        // DB에서 회원 찾기. 없다면 Exception 발생 => 이름 입력 창으로
        Member member = memberService.findMemberBySocialIdAndSocialType(signedMember.socialId(), socialType);
        JwtToken jwtToken = jwtService.issueToken(member.getId(), member.getMemberRole());
        return SignInResponse.of(member, jwtToken);
    }

    //== 회원가입(이름을 입력 받기) ==//
    @Transactional
    public SignUpServiceResponse signUp(final String socialAccessToken, final SignUpRequest signUpRequest) {
        MemberInfoServiceResponse memberInfo = getMemberInfo(signUpRequest.socialType(), socialAccessToken);

        // 이미 있는 회원인지 확인
        isNewMember(memberInfo.socialType(), memberInfo.socialId());

        // DB에 회원 저장
        Member member = memberService.saveMember(signUpRequest, memberInfo);

        // 토큰 발급
        JwtToken jwtToken = jwtService.issueToken(member.getId(), member.getMemberRole());
        return SignUpServiceResponse.of(member, jwtToken);
    }

    //== 로그아웃 ==//
    @Transactional
    public void signOut(final Long memberId) {
        // 현재 로그인된 사용자와 memberId가 일치하는지 확인 => 일치하지 않으면 Exception
        isValidMember(memberId);
        jwtService.deleteRefreshToken(memberId);
    }


    //== 회원 탈퇴 ==//
    @Transactional
    public void withdraw(final Long memberId) {
        // hard delete
        memberService.withdrawMember(memberId);
    }

    //== 토큰 재발급. redis 확인 후 재발급 ==//
    @Transactional
    public ReissueTokenServiceResponse reissueToken(final Long memberId) {
        Member member = memberService.getMemberByMemberId(memberId);
        JwtToken jwtToken = jwtService.reissueToken(memberId);

        return ReissueTokenServiceResponse.of(member, jwtToken);
    }


    //== private methods ==//

    // MemberInfoServiceResponse: socialId, socialType, email
    private MemberInfoServiceResponse getMemberInfo(SocialType socialType, String socialAccessToken) {
        return switch (socialType) {
            case KAKAO -> kakaoService.getMemberInfo(socialAccessToken);
            case GOOGLE -> googleService.getMemberInfo(socialAccessToken);
            default -> throw new BaseCustomException(AuthExceptionType.INVALID_SOCIAL_TYPE);
        };
    }


    // 현재 로그인 된 사용자와 해당 멤버 아이디가 일치하는지 확인
    private void isValidMember(Long memberId) {
        Member currentMember = memberService.getCurrentMember();
        if(!currentMember.getId().equals(memberId)) {
            throw new BaseCustomException(AuthExceptionType.FORBIDDEN);
        }
    }

    // 이미 있는 회원인지 확인하기
    private void isNewMember(SocialType socialType, String socialId) {
        memberService.checkIsNewMember(socialId, socialType);
    }

}
