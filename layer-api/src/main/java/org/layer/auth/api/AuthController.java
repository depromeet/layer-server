package org.layer.auth.api;

import lombok.RequiredArgsConstructor;
import org.layer.auth.dto.controller.*;
import org.layer.auth.dto.service.ReissueTokenServiceResponse;
import org.layer.auth.dto.service.SignInServiceResponse;
import org.layer.auth.dto.service.SignUpServiceResponse;
import org.layer.auth.jwt.JwtToken;
import org.layer.auth.service.AuthService;
import org.layer.auth.service.JwtService;
import org.layer.domain.member.entity.MemberRole;

import org.layer.oauth.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;
    private final KakaoService kakaoService;

    // 테스트용 임시 컨트롤러입니다. (토큰 없이 접속 가능)
    // "/create-token?id=멤버아이디" uri로 get 요청을 보내면 토큰이 발급됩니다.
    @GetMapping("/create-token")
    public JwtToken authTest(@RequestParam("id") Long memberId) {
        return jwtService.issueToken(memberId, MemberRole.USER);
    }

    // header에 액세스 토큰을 넣어 요청을 보내면 인증됩니다.
    @GetMapping("/authentication-test")
    public String authTest() {
        return "인증 성공";
    }

    @GetMapping("/test")
    public String test() {

        return "===";
    }

    @GetMapping("/oauth/kakao")
    public Object kakaoLogin(@RequestParam(value = "code", required = false) String code,
                             @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "error_description", required = false) String error_description,
                             @RequestParam(value = "state", required = false) String state) {

        String accessToken = kakaoService.getToken(code);
        return accessToken;
//        KakaoGetMemberInfoServiceResponse memberInfo = authService.getMemberInfo(SocialType.KAKAO, accessToken);

        // 여기서 sign-in 으로 요청보내기


    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("Authorization") final String socialAccessToken, @RequestBody final SignInRequest signInRequest)  {
        SignInServiceResponse signInServiceResponse = authService.signIn(socialAccessToken, signInRequest.socialType());
        return new ResponseEntity<>(SignInResponse.of(signInServiceResponse), HttpStatus.OK);
    }

    // 회원가입 => 소셜로그인 했는데 유효한 유저가 없을 때 이름 입력하고 회원가입하는 과정
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestHeader("Authorization") final String socialAccessToken, @RequestBody final SignUpRequest signUpRequest) {
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
}
