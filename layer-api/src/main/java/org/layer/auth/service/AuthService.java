package org.layer.auth.service;

import lombok.RequiredArgsConstructor;
import org.layer.auth.dto.service.ReissueTokenServiceResponse;
import org.layer.auth.dto.service.SignInServiceResponse;
import org.layer.auth.exception.AuthException;
//import org.layer.member.model.SocialType;
import org.layer.auth.jwt.JwtToken;
import org.layer.auth.oauth.SocialType;
import org.layer.member.Member;
import org.layer.oauth.dto.service.KakaoAccountServiceResponse;
import org.layer.oauth.service.KakaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final KakaoService kakaoLoginService;
    private final JwtService jwtService;


    // 소셜 로그인
    // TODO: SocialType 위치에 대한 고민. domain이  적절한지, 아니면 api 모듈에 있는게 적절한지
    @Transactional
    public SignInServiceResponse signIn(final String socialAccessToken, final SocialType socialType) {
        String memberEmail = getMemberInfo(socialType, socialAccessToken).email();

        // TODO: DB 조회 후 없으면 저장
        //

        Member signedMember = getMemberByEmail(memberEmail);
        JwtToken jwtToken = jwtService.issueToken(signedMember.getId(), signedMember.getMemberRole());

        return SignInServiceResponse.of(signedMember, jwtToken);
    }

    // 로그아웃
    public void signOut(final Long memberId) {
        // TODO: 현재 유저와 memberId가 동일한지 확인
        jwtService.deleteRefreshToken(memberId);
    }


    // 회원 탈퇴
    public void withdraw(final Long memberId) {
        // TODO: member 도메인에서 del_yn 바꾸기
    }

    // (리프레시 토큰을 받았을 때) 토큰 재발급
    public ReissueTokenServiceResponse reissueToken(final Long memberId) {
        // TODO: DB에서 멤버 객체 찾아오기
        Member member = new Member(); // TODO: DB에서 찾아온 객체로 바꿀것
        return ReissueTokenServiceResponse.of(member,
                jwtService.issueToken(member.getId(), member.getMemberRole()));
    }


    //== private methods ==//

    // member email 리턴
    // TODO: 리턴 타입, 접근 제어자 수정 필요
    public KakaoAccountServiceResponse getMemberInfo(SocialType socialType, String socialAccessToken) {
        return switch (socialType) {
            case KAKAO -> kakaoLoginService.getMemberInfo(socialAccessToken);
            case GOOGLE -> null;
            default -> throw new AuthException();
        };
    }

    private Member getMemberByEmail(final String email) {
        // TODO: DB에서 Member를 찾아오는 코드
        return null;
    }

    private boolean memberExists(SocialType socialType, String email) {
        // TODO: DB에서 socialType과 email로 Member를 찾아오는 코드
        return false;
    }

}
