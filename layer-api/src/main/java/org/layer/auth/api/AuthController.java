package org.layer.auth.api;

import lombok.RequiredArgsConstructor;
import org.layer.auth.dto.controller.ReissueTokenResponse;
import org.layer.auth.dto.controller.SignInRequest;
import org.layer.auth.dto.controller.SignInResponse;
import org.layer.auth.dto.service.ReissueTokenServiceResponse;
import org.layer.auth.dto.service.SignInServiceResponse;
import org.layer.auth.jwt.JwtToken;
import org.layer.auth.oauth.SocialType;
import org.layer.auth.service.AuthService;
import org.layer.auth.service.JwtService;
import org.layer.member.MemberRole;
import org.layer.oauth.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return authService.getMemberInfo(SocialType.KAKAO, accessToken);
    }



    // 로그인 TODO: 리턴타입 ResponseEntity<SuccessResponse<..>>.. 이런 식으로 변경
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("Authorization") final String socialAccessToken, @RequestBody SocialType socialType)  {
        SignInServiceResponse signInServiceResponse = authService.signIn(socialAccessToken, socialType);
        return new ResponseEntity<>(SignInResponse.of(signInServiceResponse), HttpStatus.OK);
    }


    // 로그아웃 TODO: 리턴타입 ResponseEntity<SuccessResponse>.. 으로 변경
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@RequestBody Long memberId) {
        authService.signOut(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원 탈퇴
    @PostMapping("/withdraw")
    // TODO: 리턴 타입, 리턴 데이터 수정 필요
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
