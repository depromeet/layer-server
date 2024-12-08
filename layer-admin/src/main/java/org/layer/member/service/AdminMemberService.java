package org.layer.member.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.answer.repository.AdminAnswerRepository;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.repository.AdminMemberRepository;
import org.layer.domain.space.repository.AdminMemberSpaceRelationRepository;
import org.layer.member.controller.dto.GetMemberActivityResponse;
import org.layer.member.controller.dto.GetMembersActivitiesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {
	private final AdminMemberRepository adminMemberRepository;
	private final AdminMemberSpaceRelationRepository adminMemberSpaceRelationRepository;
	private final AdminAnswerRepository adminAnswerRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	@Value("${admin.password}")
	private String password;

	public GetMembersActivitiesResponse getMemberActivities(String password, int page, int take) {


		// TODO: 검증 로직 필터단으로 옮기기
		if (!password.equals(this.password)) {
			throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
		}

		PageRequest pageRequest = PageRequest.of(page - 1, take);
		Page<Member> members = adminMemberRepository.findAll(pageRequest);


		List<GetMemberActivityResponse> responses = members.getContent().stream()
			.map(member -> {

				Long spaceCount = adminMemberSpaceRelationRepository.countAllByMemberId(member.getId());
				Long retrospectAnswerCount = adminAnswerRepository.countAllByMemberId(member.getId());


				String s = (String) redisTemplate.opsForValue().get(Long.toString(member.getId()));
				LocalDateTime recentActivityDate = s == null ? null : LocalDateTime.parse(s);


				return new GetMemberActivityResponse(member.getName(), recentActivityDate, spaceCount, retrospectAnswerCount,
					member.getCreatedAt(), member.getSocialType().name());
			}).toList();

		return new GetMembersActivitiesResponse(responses);
	}
}
