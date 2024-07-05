package org.layer.auth.api;

import lombok.RequiredArgsConstructor;
import org.layer.auth.jwt.JwtToken;
import org.layer.auth.service.JwtService;
import org.layer.member.MemberRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final JwtService jwtService;

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
}
