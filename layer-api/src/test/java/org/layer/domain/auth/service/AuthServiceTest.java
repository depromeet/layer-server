package org.layer.domain.auth.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.layer.domain.auth.controller.dto.SignUpRequest;
import org.layer.domain.auth.controller.dto.SignUpResponse;
import org.layer.domain.jwt.JwtToken;
import org.layer.domain.jwt.service.JwtService;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.layer.oauth.service.KakaoService;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @InjectMocks
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private KakaoService kakaoService;


    @Container
    static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0.8-alpine")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", () -> redisContainer.getHost());
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @BeforeAll
    static void setup() {
        redisContainer.start();
    }

    @DisplayName("동시 회원가입 요청이 들어오면 하나만 성공해야한다.")
    @Test
    void signUp_onlyOneSuccessInDupReqs() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        SocialType socialType = SocialType.KAKAO;
        String socialId = "1234567890";
        String name = "레이어";
        String email = "layer@test.com";
        String kakaoAccessToken = "fake-kakao-access-token";

        SignUpRequest signUpRequest = new SignUpRequest(socialType, name);
        MemberInfoServiceResponse memberInfoServiceResponse = new MemberInfoServiceResponse(socialId, socialType, email);

        Mockito.when(jwtService.issueToken(any(), any())).thenReturn(new JwtToken("fake-access", "fake-refresh"));
        Mockito.when(kakaoService.getMemberInfo(any())).thenReturn(memberInfoServiceResponse);

        // when : 동시 10번 회원가입 요청
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    SignUpResponse response = authService.signUp(kakaoAccessToken, signUpRequest);
                    if (response != null && response.socialId().equals(socialId)) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Optional<Member> validMember = memberRepository.findValidMember(socialId, socialType);

        // then
        assertTrue(validMember.isPresent(), "회원 가입된 멤버가 존재해야함");
        assertEquals(1, successCount.get(), "동시에 요청한 회원가입 중 하나만 성공해야 함");
        assertEquals(9, failCount.get(), "나머지 요청은 모두 실패해야 함");
    }
}