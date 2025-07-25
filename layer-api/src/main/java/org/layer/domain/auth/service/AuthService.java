package org.layer.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.layer.domain.common.random.CustomRandom;
import org.layer.event.member.SignUpEvent;
import org.layer.domain.auth.controller.dto.*;
import org.layer.domain.common.time.Time;
import org.layer.jwt.JwtToken;
import org.layer.jwt.exception.AuthException;
import org.layer.jwt.exception.AuthExceptionType;
import org.layer.jwt.service.JwtService;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.service.MemberService;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.service.OAuthService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

import static org.layer.jwt.exception.AuthExceptionType.DUPLICATED_SIGN_UP_REQUEST;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final OAuthService kakaoService;
    private final OAuthService googleService;
    private final OAuthService appleService;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final RedisTemplate<String, String> redisTemplate;

    private final ApplicationEventPublisher eventPublisher;
    private final Time time;
    private final CustomRandom random;

    private static final String SIGN_UP_LOCK_KEY = "signup:lock";


    //== 로그인 ==//
    @Transactional
    public SignInResponse signIn(final String socialAccessToken, final SocialType socialType) {
        MemberInfoServiceResponse signedMember = getMemberInfo(socialType, socialAccessToken);

        // DB에서 회원 찾기. 없다면 NEED_TO_REGISTER Exception 발생 => 이름 입력 창으로
        Member member = memberService.getMemberBySocialInfoForSignIn(signedMember.socialId(), socialType);
        JwtToken jwtToken = jwtService.issueToken(member.getId(), member.getMemberRole());
        return SignInResponse.of(member, jwtToken);
    }

    //== 회원가입(이름을 입력 받기) ==//
    @Transactional
    public SignUpResponse signUp(final String socialAccessToken, final SignUpRequest signUpRequest) {
        MemberInfoServiceResponse memberInfo = getMemberInfo(signUpRequest.socialType(), socialAccessToken);

        String lockKey = SIGN_UP_LOCK_KEY + signUpRequest.socialType() + "_" + memberInfo.socialId();
        String lockValue = UUID.randomUUID().toString();
        boolean lockAcquired = false;

        Member member = null;
        JwtToken jwtToken = null;

        try {
            // 1. Redis lock 설정
            lockAcquired = Boolean.TRUE.equals(redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, lockValue, Duration.ofSeconds(15)));

            if(!lockAcquired) {
                throw new AuthException(DUPLICATED_SIGN_UP_REQUEST);
            }

            // 2. 이미 있는 회원인지 확인
            isNewMember(memberInfo.socialType(), memberInfo.socialId());

            // 3. DB에 회원 저장
            member = memberService.saveMember(signUpRequest, memberInfo);
            publishSignUpEvent(member);

            // 4. 토큰 발급
            jwtToken = jwtService.issueToken(member.getId(), member.getMemberRole());

        } catch(AuthException e) {
            log.info("Another process is already handling signing up: {}, socialId: {}, name: {}", signUpRequest.socialType(), memberInfo.socialId(), signUpRequest.name());
            log.error("Error in signUp: {}", e.getMessage(), e);
        } finally {
            // 5. Redis lock 해제 (현재 락을 가진 주체만 해제)
            if (lockAcquired) {
                String currentLockValue = redisTemplate.opsForValue().get(lockKey);
                if (lockValue.equals(currentLockValue)) {
                    redisTemplate.delete(lockKey);
                }
            }
        }

        return SignUpResponse.of(member, jwtToken);
    }

    private void publishSignUpEvent(final Member member) {
        eventPublisher.publishEvent(new SignUpEvent(
            	random.generateRandomValue(),
				member.getId(),
				member.getName(),
                time.now(),
                member.getMemberRole().name()
        ));
    }

    //== 로그아웃 ==//
    @Transactional
    public void signOut(final Long memberId) {
        jwtService.deleteRefreshToken(memberId);
    }

    //== 회원 탈퇴 ==//
    @Transactional
    public void withdraw(final Long memberId, final WithdrawMemberRequest withdrawMemberRequest) {
        // soft delete
        memberService.withdrawMember(memberId);
    }

    //== 토큰 재발급. redis 확인 후 재발급 ==//
    @Transactional
    public ReissueTokenResponse reissueToken(final String refreshToken, final Long memberId) {
        Member member = memberService.getMemberByMemberId(memberId);
        JwtToken jwtToken = jwtService.reissueToken(refreshToken, memberId);

        return ReissueTokenResponse.of(member, jwtToken);
    }


    //== 회원 정보 얻기 ==//
    public MemberInfoResponse getMemberInfo(final Long memberId) {
        Member member = memberService.getMemberByMemberId(memberId);
        return MemberInfoResponse.of(member);
    }


    //== private methods ==//

    // MemberInfoServiceResponse: socialId, socialType, email
    private MemberInfoServiceResponse getMemberInfo(SocialType socialType, String socialAccessToken) {
        return switch (socialType) {
            case KAKAO -> kakaoService.getMemberInfo(socialAccessToken);
            case GOOGLE -> googleService.getMemberInfo(socialAccessToken);
            case APPLE -> appleService.getMemberInfo(socialAccessToken);
            default -> throw new AuthException(AuthExceptionType.INVALID_SOCIAL_TYPE);
        };
    }

    // 이미 있는 회원인지 확인하기
    private void isNewMember(SocialType socialType, String socialId) {
        memberService.checkIsNewMember(socialId, socialType);
    }

}