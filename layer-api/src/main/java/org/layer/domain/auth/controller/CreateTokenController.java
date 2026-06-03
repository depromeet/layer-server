package org.layer.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.layer.domain.auth.controller.dto.CreateTokenRequest;
import org.layer.domain.auth.controller.dto.CreateTokenResponse;
import org.layer.domain.auth.service.AuthService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("!prod")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증", description = "인증 관련 API")
public class CreateTokenController {

    private final AuthService authService;

    @PostMapping("/create-token")
    @Operation(summary = "[인증 불필요] 테스트용 토큰 생성", description = "memberId를 전달하면 100년 유효한 액세스 토큰을 발급합니다. 개발/스테이징 환경 전용이며 prod에서는 비활성화됩니다.")
    public ResponseEntity<CreateTokenResponse> createToken(@RequestBody CreateTokenRequest request) {
        return ResponseEntity.ok(authService.createToken(request.memberId()));
    }
}
