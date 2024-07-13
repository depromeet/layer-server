package org.layer.domain.retrospect.service;

import java.util.List;

import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.retrospect.service.dto.response.RetrospectGetServiceResponse;
import org.layer.domain.retrospect.service.dto.response.RetrospectListGetServiceResponse;
import org.layer.domain.space.entity.MemberSpaceRelations;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetrospectService {

	private final RetrospectRepository retrospectRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final AnswerRepository answerRepository;

	@Transactional
	public void create(Long spaceId, Long formId, String title, String introduction) {
		Retrospect retrospect = Retrospect.builder()
			.title(title)
			.formId(formId)
			.spaceId(spaceId)
			.introduction(introduction)
			.retrospectStatus(RetrospectStatus.PROCEEDING)
			.build();

		retrospectRepository.save(retrospect);
	}

	public RetrospectListGetServiceResponse getRetrospects(Long spaceId, Long memberId) {

		MemberSpaceRelations memberSpaceRelations = new MemberSpaceRelations(
			memberSpaceRelationRepository.findAllBySpaceId(spaceId));

		// 해당 멤버가 요청한 스페이스 소속 여부 확인
		memberSpaceRelations.validateTeamMembership(memberId);

		List<Retrospect> retrospects = retrospectRepository.findAllBySpaceId(spaceId);
		List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
		Answers answers = new Answers(answerRepository.findAllByRetrospectIdIn(retrospectIds));

		List<RetrospectGetServiceResponse> retrospectDtos = retrospects.stream()
			.map(r -> RetrospectGetServiceResponse.of(r.getTitle(), r.getIntroduction(),
				answers.hasRetrospectAnswer(memberId), r.getRetrospectStatus(), answers.getWriteCount()))
			.toList();

		return RetrospectListGetServiceResponse.of(retrospects.size(), retrospectDtos);
	}
}
