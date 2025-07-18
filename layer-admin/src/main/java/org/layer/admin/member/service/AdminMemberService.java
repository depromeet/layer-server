package org.layer.admin.member.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.layer.admin.member.controller.dto.MemberSignupCountResponse;
import org.layer.admin.member.entity.AdminMemberRole;
import org.layer.admin.member.entity.AdminMemberSignupHistory;
import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.event.member.SignUpEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

	private final AdminMemberRepository adminMemberRepository;

	public List<MemberSignupCountResponse> getMemberSignupCount(LocalDateTime startDate, LocalDateTime endDate) {
		List<AdminMemberSignupHistory> histories = adminMemberRepository.findAllByEventTimeBetween(
			startDate, endDate);

		Map<LocalDate, Long> countMap = histories.stream()
			.collect(Collectors.groupingBy(
				h -> h.getEventTime().toLocalDate(),
				Collectors.counting()
			));

		List<MemberSignupCountResponse> result = new ArrayList<>();

		// startDate ~ endDate 사이를 하루 단위로 순회
		LocalDate current = startDate.toLocalDate();
		LocalDate end = endDate.toLocalDate();

		while (!current.isAfter(end)) {
			long count = countMap.getOrDefault(current, 0L);
			result.add(new MemberSignupCountResponse(current, (int) count));
			current = current.plusDays(1);
		}

		return result;
	}

	@Transactional
	public void saveMemberSignUp(SignUpEvent event) {
		AdminMemberSignupHistory adminMemberSignupHistory = AdminMemberSignupHistory.builder()
			.eventId(event.eventId())
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.memberRole(AdminMemberRole.fromString(event.memberRole()))
			.build();

		adminMemberRepository.save(adminMemberSignupHistory);
	}
}
