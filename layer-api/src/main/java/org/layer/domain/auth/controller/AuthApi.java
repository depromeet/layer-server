package org.layer.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "토큰 재발급", description = "member_id를 전달하면 데이터베이스에 리프레시 토큰이 남아있지 확인하고 남아있다면 jwt(access + refresh)를 새로 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "토큰 재발급 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="토큰 재발급 성공(리프레시 토큰이 DB에 남아있음, 기한 2주)", value = """
                    {
                      "memberId": 1,
                      "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjA2OTMyOTMsImV4cCI6MTcyMDY5NTA5Mywicm9sZSI6WyJVU0VSIl0sIm1lbWJlcklkIjoxfQ.nt4Tj1jTihS-6U7j2wkzv4VbgzTkhSPWnjBC_yXe_GiOKn3eoJ0i9NuA7Dzw6e4w_B-ab_PHzdrhfzyeVoPJOg",
                      "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjA2OTMyOTMsImV4cCI6MTcyMTkwMjg5Mywicm9sZSI6WyJVU0VSIl0sIm1lbWJlcklkIjoxfQ.MROa3B266VcnQqGHpvu2Lh3JiwexOM4BTYQt_3Tbc7xMY1AwS5Z51oAyVVZdO7wTLDLiUNe73DwR-7HNejWEdA"
                    }
                    """
                            )
                    })),
            @ApiResponse(responseCode = "404", description = "토큰 재발급 실패",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="토큰 재발급 실패(리프레시 토큰이 DB에 없음)", value = """
                    {
                      "name": "NOT_FOUND_USER",
                      "message": "유효한 유저를 찾지 못했습니다."
                    }
                    """
                            )
                    }))
    })
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest);
}
