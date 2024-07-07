package org.layer.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.auth.controller.dto.SignUpRequest;
import org.layer.domain.auth.exception.AuthException;
import org.layer.domain.auth.service.dto.ReissueTokenServiceResponse;
import org.layer.domain.auth.service.dto.SignInServiceResponse;
import org.layer.domain.auth.service.dto.SignUpServiceResponse;
import org.layer.domain.jwt.JwtToken;
import org.layer.domain.jwt.service.JwtService;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.service.MemberService;
import org.layer.domain.member.service.MemberUtil;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.service.GoogleService;
import org.layer.oauth.service.KakaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.layer.common.exception.MemberExceptionType.FORBIDDEN;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final MemberUtil memberUtil;

    //== 로그인 ==//
    @Transactional
    public SignInServiceResponse signIn(final String socialAccessToken, final SocialType socialType) {
        MemberInfoServiceResponse signedMember = getMemberInfo(socialType, socialAccessToken);

        // DB에서 회원 찾기. 없다면 Exception 발생 => 이름 입력 창으로
        Member member = memberService.findMemberBySocialIdAndSocialType(signedMember.socialId(), socialType);
        JwtToken jwtToken = jwtService.issueToken(member.getId(), member.getMemberRole());
        return SignInServiceResponse.of(member, jwtToken);
    }

    //== 회원가입(이름을 입력 받기) ==//
    @Transactional
    public SignUpServiceResponse signUp(final String socialAccessToken, final SignUpRequest signUpRequest) {
        MemberInfoServiceResponse memberInfo = getMemberInfo(signUpRequest.socialType(), socialAccessToken);

        // 이미 있는 회원인지 확인
        isNewMember(memberInfo.socialType(), memberInfo.socialId());

        // DB에 회원 저장
        Member member = memberService.saveMember(signUpRequest, memberInfo);
        return SignUpServiceResponse.of(member);
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
        // TODO: member 도메인에서 del_yn 바꾸기 => Member entitiy에 추가,,?

    }

    //== (리프레시 토큰을 받았을 때) 토큰 재발급 ==//
    @Transactional
    public ReissueTokenServiceResponse reissueToken(final Long memberId) {
        // 현재 로그인된 사용자와 memberId가 일치하는지 확인
        isValidMember(memberId);

        // 시큐리티 컨텍스트에서 member 찾아오기
        Member member = memberUtil.getCurrentMember();
        return ReissueTokenServiceResponse.of(member,
                jwtService.issueToken(member.getId(), member.getMemberRole()));
    }


    //== private methods ==//

    // MemberInfoServiceResponse: socialId, socialType, email
    private MemberInfoServiceResponse getMemberInfo(SocialType socialType, String socialAccessToken) {
        return switch (socialType) {
            case KAKAO -> kakaoService.getMemberInfo(socialAccessToken);
            case GOOGLE -> googleService.getMemberInfo(socialAccessToken);
            default -> throw new AuthException();
        };
    }


    // 현재 로그인 된 사용자와 해당 멤버 아이디가 일치하는지 확인
    private void isValidMember(Long memberId) {
        Member currentMember = memberUtil.getCurrentMember();
        if(!currentMember.getId().equals(memberId)) {
            throw new BaseCustomException(FORBIDDEN);
        }
    }

    // 이미 있는 회원인지 확인하기
    private void isNewMember(SocialType socialType, String socialId) {
        memberService.checkIsNewMember(socialId, socialType);
    }

}
