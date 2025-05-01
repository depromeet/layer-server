package org.layer.domain.member.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.layer.domain.auth.controller.dto.SignUpRequest;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.exception.MemberException;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @DisplayName("탈퇴한 회원은 조회되지 않는다.")
    @Test
    void withdrawTest() {
        // given
        String socialId = "12345678";
        String email = "layer@example.com";
        String username = "레이어";
        SocialType socialType = SocialType.KAKAO;

        SignUpRequest signUpRequest = new SignUpRequest(socialType, username);
        MemberInfoServiceResponse memberInfoServiceResponse = new MemberInfoServiceResponse(socialId, socialType, email);

        // when
        Member member = memberService.saveMember(signUpRequest, memberInfoServiceResponse);
        memberService.withdrawMember(member.getId());
        Optional<Member> findMember = memberRepository.findValidMember(socialId, socialType);

        // then
        assertThat(findMember).isEmpty();
        Assertions.assertThrows(MemberException.class, () -> memberRepository.findValidMemberByIdOrThrow(member.getId()));
    }

    @DisplayName("탈퇴 후 같은 회원이 같은 소셜 로그인 방식으로 재가입할 수 있다.")
    @Test
    void withdrawAndRejoin() {
        // given
        String socialId = "12345678";
        String email = "layer@example.com";
        String username = "레이어";
        SocialType socialType = SocialType.KAKAO;

        SignUpRequest signUpRequest = new SignUpRequest(socialType, username);
        MemberInfoServiceResponse memberInfoServiceResponse = new MemberInfoServiceResponse(socialId, socialType, email);

        // when
        Member withdrawMember = memberService.saveMember(signUpRequest, memberInfoServiceResponse);
        memberService.withdrawMember(withdrawMember.getId());

        Member rejoinMember = memberService.saveMember(signUpRequest, memberInfoServiceResponse);


        // then
        assertThat(withdrawMember).isNotEqualTo(rejoinMember);
        assertThat(rejoinMember.getDeletedAt()).isNull();
        assertThat(withdrawMember.getId()).isNotEqualTo(rejoinMember.getId());
    }

}