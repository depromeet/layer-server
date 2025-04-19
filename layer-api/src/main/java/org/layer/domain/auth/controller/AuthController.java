package org.layer.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.annotation.DisableSwaggerSecurity;
import org.layer.annotation.MemberId;
import org.layer.domain.auth.controller.dto.*;
import org.layer.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    private static final String SOCIAL_TOKEN_NAME = "X-AUTH-TOKEN";

    // 로그인
    @Override
    @DisableSwaggerSecurity
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader(SOCIAL_TOKEN_NAME) final String socialAccessToken, @RequestBody final SignInRequest signInRequest) {
        SignInResponse signInResponse = authService.signIn(socialAccessToken, signInRequest.socialType());
        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }

    // 회원가입 => 소셜로그인 했는데 유효한 유저가 없을 때 이름 입력하고 회원가입하는 과정
    @Override
    @DisableSwaggerSecurity
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestHeader(SOCIAL_TOKEN_NAME) final String socialAccessToken, @RequestBody final SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = authService.signUp(socialAccessToken, signUpRequest);
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }


    // 로그아웃
    @Override
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@RequestBody SignOutRequest signOutRequest) {
        authService.signOut(signOutRequest.memberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원 탈퇴
    @Override
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@MemberId Long memberId, @Valid @RequestBody WithdrawMemberRequest withdrawMemberRequest) {
        authService.withdraw(memberId, withdrawMemberRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 토큰 재발급
    @Override
    @PostMapping("/reissue-token")
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestHeader(value = "Refresh", required = false) String refreshToken, ReissueTokenRequest reissueTokenRequest) {
        ReissueTokenResponse response = authService.reissueToken(refreshToken, reissueTokenRequest.memberId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // 액세스 토큰으로 회원 정보 얻기
    @Override
    @GetMapping("/member-info")
    public MemberInfoResponse getMemberInfo(@MemberId Long memberId) {
        return authService.getMemberInfo(memberId);
    }
}
