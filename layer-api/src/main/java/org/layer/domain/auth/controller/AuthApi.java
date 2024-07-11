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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "인증", description = "인증 관련 API")
public interface AuthApi {
    @Operation(summary = "[인증 불필요] 로그인", description = "소셜 로그인 API(구글, 카카오), 헤더에 소셜 액세스 토큰이 필요하며, 자체 jwt 필요없음")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    headers = {
                            @Header(name = "Authorization", description = "소셜 액세스 토큰(Bearer 없이 토큰만)", schema = @Schema(type = "string", format = "jwt"), required = true)
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="로그인 성공", value = """
                    {
                        "memberId": 1,
                        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjA2OTcyMDksImV4cCI6MTcyMDY5OTAwOSwicm9sZSI6WyJVU0VSIl0sIm1lbWJlcklkIjoxfQ.OV-RWbIPZIQlMsPMR0reFHMFq9MNBKQwf7Hw7Uo0QbJPrTEACu0MqSJlv-gMtag1PhBxo7KB5dxEDza6QI06Zw",
                        "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjA2OTcyMTAsImV4cCI6MTcyMTkwNjgxMCwicm9sZSI6WyJVU0VSIl0sIm1lbWJlcklkIjoxfQ.fIVauBlL3GHLrVFJ1YwWb89RFwxa84Cql2WqEu4L258ebPJ04TkAGbqrCt7i-oEKI6dbvv0KDRKXkgDQH18kTA",
                        "memberRole": "USER"
                    }
                    """
                            )
                    })),
            @ApiResponse(responseCode = "400", description = "로그인 실패 - 토큰이 유효하지 않음",
                    headers = {
                            @Header(name = "Authorization", description = "소셜 액세스 토큰(Bearer 없이 토큰만)", schema = @Schema(type = "string", format = "jwt"), required = true)
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="토큰이 유효하지 않음", value = """
                    {
                        "name": "FAIL_TO_AUTH",
                        "message": "인증에 실패했습니다."
                    }
                    """
                            )
                    })),
            @ApiResponse(responseCode = "404", description = "로그인 실패 - 회원이 DB에 없음",
                    headers = {
                            @Header(name = "Authorization", description = "소셜 액세스 토큰(Bearer 없이 토큰만)", schema = @Schema(type = "string", format = "jwt"), required = true)
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="회원이 DB에 없음", value = """
                    {
                        "name": "NOT_FOUND_USER",
                        "message": "유효한 유저를 찾지 못했습니다."
                    }
                    """
                            )
                    }))
    })
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("Authorization") final String socialAccessToken, @RequestBody final SignInRequest signInRequest);

    @Operation(summary = "[인증 불필요] 회원가입", description = "처음 소셜 로그인 하는 유저가 이름을 입력하는 과정, social_type은 KAKAO, GOOGLE")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    headers = {
                            @Header(name = "Authorization", description = "소셜 액세스 토큰(Bearer 없이 토큰만)", schema = @Schema(type = "string", format = "jwt"), required = true)
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="회원 가입 성공. 유저의 정보를 리턴", value = """
                    {
                        "memberId": 1,
                        "name": "김회고",
                        "email": "layerkim@kakao.com",
                        "memberRole": "USER",
                        "SocialId": "1234567890",
                        "socialType": "KAKAO"
                    }
                    """
                            )
                    })),
            @ApiResponse(responseCode = "400", description = "회원가입 실패",
                    headers = {
                            @Header(name = "Authorization", description = "소셜 액세스 토큰", schema = @Schema(type = "string", format = "jwt"), required = true)
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="이미 가입된 회원", value = """
                            {
                                "name": "NOT_A_NEW_MEMBER",
                                "message": "이미 가입된 회원입니다."
                            }
                    """
                            ),
                            @ExampleObject(name="토큰이 유효하지 않음", value = """
                            {
                                "name": "FAIL_TO_AUTH",
                                "message": "인증에 실패했습니다."
                            }
                            """)
                    }))
    })
    public ResponseEntity<SignUpResponse> signUp(@RequestHeader("Authorization") final String socialAccessToken, @RequestBody final SignUpRequest signUpRequest);

    @Operation(summary = "로그아웃", description = "member_id를 전달하면 DB에서 리프레시 토큰을 지웁니다.")
    public ResponseEntity<?> signOut(SignOutRequest signOutRequest);

    @Operation(summary = "[인증 불필요] 토큰 재발급", description = "member_id를 전달하면 데이터베이스에 리프레시 토큰이 남아있지 확인하고 남아있다면 jwt(access + refresh)를 새로 발급합니다.")
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
            @ApiResponse(responseCode = "401", description = "토큰 재발급 실패",
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


    @Operation(summary = "회원 탈퇴", description = "header Authorization에 액세스 토큰과 memberId를 전달하여 회원 탈퇴를 할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "탈퇴 성공",
            headers = {
                    @Header(name = "Authorization", description = "자체 jwt 액세스 토큰", schema = @Schema(type = "string", format = "jwt"), required = true)
            })
    public ResponseEntity<?> withdraw(WithdrawMemberRequest withdrawMemberRequest);

    // TODO: 토큰 확인용 임시 API 추후 삭제
    @Operation(summary = "[실제 사용 X] 구글 액세스 토큰 받기", description = "서버 쪽에서 토큰을 확인하기 위한 API입니다! (실제 사용 X, 추후 삭제 예정)")
    public String googleTest(@RequestParam("code") String code);

    // TODO: 토큰 확인용 임시 API 추후 삭제
    @Operation(summary = "[실제 사용 X] 카카오 액세스 토큰 받기", description = "서버 쪽에서 토큰을 확인하기 위한 API입니다! (실제 사용 X, 추후 삭제 예정)")
    public Object kakaoLogin(@RequestParam(value = "code", required = false) String code);


}
