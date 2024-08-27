package org.layer.domain.analyze.service;

import static org.layer.domain.answer.entity.Answers.*;

import java.util.ArrayList;
import java.util.List;

import org.layer.domain.analyze.controller.dto.response.AnalyzeIndividualGetResponse;
import org.layer.domain.analyze.controller.dto.response.AnalyzeTeamGetResponse;
import org.layer.domain.analyze.controller.dto.response.AnalyzesGetResponse;
import org.layer.domain.analyze.entity.Analyze;
import org.layer.domain.analyze.enums.AnalyzeType;
import org.layer.domain.analyze.repository.AnalyzeRepository;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.external.ai.service.AIAnalyzeService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AnalyzeService {

	private final AnalyzeRepository analyzeRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final SpaceRepository spaceRepository;

	private final AIAnalyzeService aiAnalyzeService;

	@Transactional
	@Async
	public void createAnalyzeTemp(Long spaceId, Long retrospectId, List<Long> memberIds) {
		aiAnalyzeService.createAnalyze(spaceId, retrospectId, memberIds);
	}

	public AnalyzesGetResponse getAnalyze(Long spaceId, Long retrospectId, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		Space space = spaceRepository.findByIdOrThrow(spaceId);

		AnalyzeTeamGetResponse analyzeTeamGetResponse = null;
		if(space.getCategory().equals(SpaceCategory.TEAM)) {
			Analyze teamAnalyze = analyzeRepository.findByRetrospectIdAndAnalyzeTypeOrThrow(retrospectId,
				AnalyzeType.TEAM);
			analyzeTeamGetResponse = AnalyzeTeamGetResponse.of(teamAnalyze);
		}

		Analyze individualAnalyze = analyzeRepository.findByRetrospectIdAndAnalyzeTypeAndMemberIdOrThrow(retrospectId,
			AnalyzeType.INDIVIDUAL, memberId);

		return AnalyzesGetResponse.of(analyzeTeamGetResponse, AnalyzeIndividualGetResponse.of(individualAnalyze));
	}

}
