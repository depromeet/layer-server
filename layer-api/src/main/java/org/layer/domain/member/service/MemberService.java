package org.layer.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.MemberExceptionType;
import org.layer.domain.analyze.entity.Analyze;
import org.layer.domain.analyze.entity.AnalyzeDetail;
import org.layer.domain.analyze.enums.AnalyzeDetailType;
import org.layer.domain.analyze.repository.AnalyzeRepository;
import org.layer.domain.auth.controller.dto.SignUpRequest;
import org.layer.domain.common.time.Time;
import org.layer.domain.external.google.enums.SheetType;
import org.layer.domain.external.google.service.GoogleApiService;
import org.layer.domain.jwt.SecurityUtil;
import org.layer.domain.member.controller.dto.CreateFeedbackRequest;
import org.layer.domain.member.controller.dto.GetMemberAnalyzesResponse;
import org.layer.domain.member.controller.dto.GetMemberRecentAnalyzeResponse;
import org.layer.domain.member.controller.dto.GetMemberRecentBadAnalyzeResponse;
import org.layer.domain.member.controller.dto.GetMemberRecentGoodAnalyzeResponse;
import org.layer.domain.member.controller.dto.GetMemberRecentImprovementAnalyzeResponse;
import org.layer.domain.member.controller.dto.UpdateMemberInfoRequest;
import org.layer.domain.member.controller.dto.UpdateMemberInfoResponse;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.MemberFeedback;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.retrospect.dto.SpaceRetrospectDto;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.oauth.dto.service.MemberInfoServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.layer.common.exception.MemberExceptionType.NOT_A_NEW_MEMBER;
import static org.layer.common.exception.MemberExceptionType.NOT_FOUND_USER;
import static org.layer.domain.member.entity.MemberRole.USER;

@RequiredArgsConstructor
@Service
public class MemberService {
private static final int TWO_MONTHS = 2;

	private final MemberRepository memberRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final RetrospectRepository retrospectRepository;
	private final AnalyzeRepository analyzeRepository;

	private final GoogleApiService googleApiService;

	private final SecurityUtil securityUtil;

	private final Time time;

	// 소셜 아이디와 소셜 타입으로 멤버 찾기. 멤버가 없으면 Exception
	// 현재는 사용하지 않음
	public Member getMemberBySocialIdAndSocialType(String socialId, SocialType socialType) {
		return memberRepository.findBySocialIdAndSocialType(socialId, socialType)
			.orElseThrow(() -> new BaseCustomException(MemberExceptionType.NOT_FOUND_USER));
	}

	// sign-in만을 위한 메서드. 멤버가 없을시 회원가입이 필요함을 알려준다.
	// 회원이 진짜로 없는 error의 경우와 회원 가입이 필요하다는 응답을 구분하기 위함
	public Member getMemberBySocialInfoForSignIn(String socialId, SocialType socialType) {
		return memberRepository.findBySocialIdAndSocialType(socialId, socialType)
			.orElseThrow(() -> new BaseCustomException(MemberExceptionType.NEED_TO_REGISTER));
	}

	public void checkIsNewMember(String socialId, SocialType socialType) {
		Optional<Member> memberOpt = memberRepository.findBySocialIdAndSocialType(socialId, socialType);

		if (memberOpt.isPresent()) {
			throw new BaseCustomException(NOT_A_NEW_MEMBER);
		}
	}

	public void createFeedback(Long memberId, CreateFeedbackRequest createFeedbackRequest) {
		var foundMemberFeedback = findFeedback(memberId);
		if (foundMemberFeedback.isEmpty()) {
			return;
		}
		googleApiService.writeFeedback(SheetType.FEEDBACK, foundMemberFeedback.get(),
			createFeedbackRequest.satisfaction(), createFeedbackRequest.description());
	}

	@Transactional
	public Member saveMember(SignUpRequest signUpRequest, MemberInfoServiceResponse memberInfo) {
		Member member = Member.builder()
			.name(signUpRequest.name())
			.memberRole(USER)
			.email(memberInfo.email())
			.socialId(memberInfo.socialId())
			.socialType(memberInfo.socialType())
			.build();

		memberRepository.save(member);

		return member;
	}

	public Member getCurrentMember() {
		return memberRepository
			.findById(securityUtil.getCurrentMemberId())
			.orElseThrow(() -> new BaseCustomException(NOT_FOUND_USER));
	}

	public Member getMemberByMemberId(Long memberId) {
		return memberRepository.
			findById(memberId)
			.orElseThrow(() -> new BaseCustomException(NOT_FOUND_USER));
	}

	@Transactional
	public void withdrawMember(Long memberId) {
		Member currentMember = getCurrentMember();
		memberRepository.delete(currentMember);
	}

	@Transactional
	public UpdateMemberInfoResponse updateMemberInfo(Long memberId, UpdateMemberInfoRequest updateMemberInfoRequest) {
		Member member = memberRepository.findByIdOrThrow(memberId);
		member.updateName(updateMemberInfoRequest.name());
		member.updateProfileImageUrl(updateMemberInfoRequest.profileImageUrl());

		return UpdateMemberInfoResponse.builder()
			.memberId(member.getId())
			.name(member.getName())
			.profileImageUrl(member.getProfileImageUrl())
			.build();
	}

	public Optional<MemberFeedback> findFeedback(Long memberId) {
		return memberRepository.findAllMemberFeedback(memberId);
	}

	@Transactional(readOnly = true)
	public GetMemberAnalyzesResponse getMyAnalyzes(Long memberId) {
		List<MemberSpaceRelation> memberSpaceRelations = memberSpaceRelationRepository.findAllByMemberId(memberId);
		List<Long> spaceIds = memberSpaceRelations.stream().map(m -> m.getSpace().getId()).toList();

		List<SpaceRetrospectDto> recentRetrospects = new ArrayList<>();
		spaceIds.forEach(spaceId -> {
				Optional<SpaceRetrospectDto> spaceRetrospectDto = retrospectRepository.findFirstBySpaceIdAndRetrospectStatusAndDeadlineAfterOrderByDeadline(
					spaceId, RetrospectStatus.DONE, time.now().minusMonths(TWO_MONTHS));
				spaceRetrospectDto.ifPresent(recentRetrospects::add);
			});

		List<Long> retrospectIds = recentRetrospects.stream().map(r -> r.getRetrospect().getId()).toList();
		Map<Long, SpaceRetrospectDto> spaceRetrospectDtoMap = recentRetrospects.stream()
			.collect(Collectors.toMap(r -> r.getRetrospect().getId(), r -> r));

		List<Analyze> analyzes = analyzeRepository.findAllByMemberIdAndRetrospectIdInQuery(memberId, retrospectIds);

		return getMemberAnalyzeResponseDto(spaceRetrospectDtoMap, analyzes);
	}

	private GetMemberAnalyzesResponse getMemberAnalyzeResponseDto(Map<Long, SpaceRetrospectDto> spaceRetrospectDtoMap, List<Analyze> analyzes) {
		List<GetMemberRecentAnalyzeResponse> recentAnalyzes = new ArrayList<>();
		List<GetMemberRecentGoodAnalyzeResponse> goodAnalyzes = new ArrayList<>();
		List<GetMemberRecentBadAnalyzeResponse> badAnalyzes = new ArrayList<>();
		List<GetMemberRecentImprovementAnalyzeResponse> improvementAnalyzes = new ArrayList<>();

		analyzes.forEach(analyze -> {
			AnalyzeDetail goodAnalyzeDetail = analyze.getTopCountAnalyzeDetailBy(AnalyzeDetailType.GOOD);
			AnalyzeDetail badAnalyzeDetail = analyze.getTopCountAnalyzeDetailBy(AnalyzeDetailType.BAD);
			AnalyzeDetail improvementAnalyzeDetail = analyze.getTopCountAnalyzeDetailBy(AnalyzeDetailType.IMPROVEMENT);
			SpaceRetrospectDto spaceRetrospectDto = spaceRetrospectDtoMap.get(analyze.getRetrospectId());

			recentAnalyzes.add(GetMemberRecentAnalyzeResponse.of(spaceRetrospectDto));
			goodAnalyzes.add(GetMemberRecentGoodAnalyzeResponse.of(spaceRetrospectDto, goodAnalyzeDetail.getContent()));
			badAnalyzes.add(GetMemberRecentBadAnalyzeResponse.of(spaceRetrospectDto, badAnalyzeDetail.getContent()));
			improvementAnalyzes.add(GetMemberRecentImprovementAnalyzeResponse.of(spaceRetrospectDto,
				improvementAnalyzeDetail.getContent()));
		});

		return GetMemberAnalyzesResponse.of(recentAnalyzes, goodAnalyzes, badAnalyzes, improvementAnalyzes);
	}
}
