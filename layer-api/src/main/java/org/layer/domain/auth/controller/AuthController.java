package org.layer.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.auth.controller.dto.*;
import org.layer.domain.auth.service.AuthService;
import org.layer.domain.auth.service.dto.SignInServiceResponse;
import org.layer.domain.auth.service.dto.SignUpServiceResponse;
import org.layer.oauth.service.GoogleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final GoogleService googleService;

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("Authorization") final String socialAccessToken, @RequestBody final SignInRequest signInRequest)  {
        SignInServiceResponse signInServiceResponse = authService.signIn(socialAccessToken, signInRequest.socialType());
        return new ResponseEntity<>(SignInResponse.of(signInServiceResponse), HttpStatus.OK);
    }

    // 회원가입 => 소셜로그인 했는데 유효한 유저가 없을 때 이름 입력하고 회원가입하는 과정
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestHeader(value = "authorizatio") final String socialAccessToken, @RequestBody final SignUpRequest signUpRequest) {
        log.info("{} <<< socialAccessToken",socialAccessToken);
        SignUpServiceResponse signUpServiceResponse = authService.signUp(socialAccessToken, signUpRequest);
        return new ResponseEntity<>(SignUpResponse.of(signUpServiceResponse), HttpStatus.CREATED);
    }


    // 로그아웃
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@RequestBody Long memberId) {
        authService.signOut(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원 탈퇴
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Long memberId) {
        authService.withdraw(memberId);
        return new ResponseEntity<>(HttpStatus.OK); // TODO: 리턴 객체 수정 필요
    }

    // 토큰 재발급
    @PostMapping("/reissue-token")
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestBody Long memberId) {
        return new ResponseEntity<>(
                ReissueTokenResponse.of(authService.reissueToken(memberId)),
                HttpStatus.CREATED);
    }


//    //== 구글 테스트용 api ==//
//    @GetMapping("oauth2/google")
//    public String googleTest(@RequestParam("code") String code) {
//        log.info("code : " + code);
//        String token = googleService.getToken(code);
//        return token;
//    }


}
