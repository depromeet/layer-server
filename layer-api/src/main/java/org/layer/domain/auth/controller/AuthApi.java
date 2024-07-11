package org.layer.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.domain.auth.controller.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "인증", description = "인증 관련 API")
public interface AuthApi {
    @Operation(summary = "로그인", description = "소셜 로그인 API(구글, 카카오")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("Authorization") final String socialAccessToken, @RequestBody final SignInRequest signInRequest);

    @Operation(summary = "로그아웃")
    public ResponseEntity<?> signOut(@RequestBody Long memberId);

//    @Operation(summary = "토큰 재발급", description = "member_id를 전달하면 데이터베이스에 리프레시 토큰이 남아있지 확인하고 남아있다면 jwt(access + refresh)를 새로 발급합니다.")
//    @ApiResponse(responseCode = "201", description="토큰 재발급 성공",
//        content = @Content(mediaType = "application/json"), examples = {
//            @ExampleObject(name="토큰 재발급 성공")
//    })
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest);
}
