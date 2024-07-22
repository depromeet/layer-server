package org.layer.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.DisableSwaggerSecurity;
import org.layer.common.annotation.MemberId;
import org.layer.domain.auth.controller.dto.*;
import org.layer.domain.auth.service.AuthService;
import org.layer.domain.auth.controller.dto.SignInResponse;
import org.layer.domain.auth.service.dto.SignUpServiceResponse;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.oauth.service.GoogleService;
import org.layer.oauth.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final GoogleService googleService;
    private final KakaoService kakaoService;
    private final MemberRepository memberRepository;

    private static final String SOCIAL_TOKEN_NAME = "X-AUTH-TOKEN";

    // 로그인
    @DisableSwaggerSecurity
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader(SOCIAL_TOKEN_NAME) final String socialAccessToken, @RequestBody final SignInRequest signInRequest) {
        SignInResponse signInResponse = authService.signIn(socialAccessToken, signInRequest.socialType());
        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }

    // 회원가입 => 소셜로그인 했는데 유효한 유저가 없을 때 이름 입력하고 회원가입하는 과정
    @DisableSwaggerSecurity
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestHeader(SOCIAL_TOKEN_NAME) final String socialAccessToken, @RequestBody final SignUpRequest signUpRequest) {
        SignUpServiceResponse signUpServiceResponse = authService.signUp(socialAccessToken, signUpRequest);
        return new ResponseEntity<>(SignUpResponse.of(signUpServiceResponse), HttpStatus.CREATED);
    }


    // 로그아웃
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@RequestBody SignOutRequest signOutRequest) {
        authService.signOut(signOutRequest.memberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원 탈퇴
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(WithdrawMemberRequest withdrawMemberRequest) {
        authService.withdraw(withdrawMemberRequest.memberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 토큰 재발급
    @PostMapping("/reissue-token")
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestHeader(value = "Refresh", required = false) String refreshToken,
                                                             ReissueTokenRequest reissueTokenRequest) {
        return new ResponseEntity<>(
                ReissueTokenResponse.of(authService.reissueToken(refreshToken, reissueTokenRequest.memberId())),
                HttpStatus.CREATED);
    }

    // 액세스 토큰으로 회원 정보 얻기
    @GetMapping("/member-info")
    public MemberInfoResponse getMemberInfo(@MemberId Long memberId) {
        return authService.getMemberInfo(memberId);
    }

    @DisableSwaggerSecurity
    //== google OAuth2 test용 API 액세스 토큰 발급 ==//
    @GetMapping("/oauth2/google")
    public String googleTest(@RequestParam("code") String code) {
        return googleService.getToken(code);
    }

    @DisableSwaggerSecurity
    //== kakao OAuth2 test용 API 액세스 토큰 발급 ==//
    @GetMapping("/oauth2/kakao")
    public Object kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        return kakaoService.getToken(code);
    }
}
