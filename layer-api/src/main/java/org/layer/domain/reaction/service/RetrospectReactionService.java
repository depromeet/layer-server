package org.layer.domain.reaction.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.answer.entity.Answer;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.reaction.controller.dto.request.RetrospectReactionCreateRequest;
import org.layer.domain.reaction.controller.dto.response.AnswerReactionGetResponse;
import org.layer.domain.reaction.controller.dto.response.ReactionGetResponse;
import org.layer.domain.reaction.controller.dto.response.ReactionListGetResponse;
import org.layer.domain.reaction.controller.dto.response.RetrospectReactionElementResponse;
import org.layer.domain.reaction.controller.dto.response.RetrospectReactionListGetResponse;
import org.layer.domain.reaction.entity.Reaction;
import org.layer.domain.reaction.entity.RetrospectReaction;
import org.layer.domain.reaction.exception.ReactionException;
import org.layer.domain.reaction.repository.ReactionRepository;
import org.layer.domain.reaction.repository.RetrospectReactionRepository;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.member.entity.Members;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.layer.global.exception.ReactionExceptionType.ALREADY_REACTED;
import static org.layer.global.exception.ReactionExceptionType.FORBIDDEN_REACTION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetrospectReactionService {

    private final ReactionRepository reactionRepository;
    private final RetrospectReactionRepository retrospectReactionRepository;
    private final AnswerRepository answerRepository;
    private final RetrospectRepository retrospectRepository;
    private final MemberRepository memberRepository;
    private final MemberSpaceRelationRepository memberSpaceRelationRepository;

    @Transactional
    public Long createReaction(Long spaceId, Long retrospectId, RetrospectReactionCreateRequest request, Long memberId) {
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        retrospectRepository.findByIdOrThrow(retrospectId);

        reactionRepository.findByIdOrThrow(request.reactionId());

        if (retrospectReactionRepository.existsByAnswerIdAndMemberId(request.answerId(), memberId)) {
            throw new ReactionException(ALREADY_REACTED);
        }

        RetrospectReaction retrospectReaction = RetrospectReaction.builder()
                .reactionId(request.reactionId())
                .answerId(request.answerId())
                .memberId(memberId)
                .build();

        return retrospectReactionRepository.save(retrospectReaction).getId();
    }

    @Transactional
    public void deleteReaction(Long spaceId, Long retrospectId, Long retrospectReactionId, Long memberId) {
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        retrospectRepository.findByIdOrThrow(retrospectId);

        RetrospectReaction retrospectReaction = retrospectReactionRepository.findByIdOrThrow(retrospectReactionId);

        if (!retrospectReaction.getMemberId().equals(memberId)) {
            throw new ReactionException(FORBIDDEN_REACTION);
        }

        retrospectReactionRepository.delete(retrospectReaction);
    }

    public RetrospectReactionListGetResponse getRetrospectReactions(Long spaceId, Long retrospectId, Long memberId) {
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        retrospectRepository.findByIdOrThrow(retrospectId);

        List<Long> answerIds = answerRepository.findAllByRetrospectId(retrospectId)
                .stream()
                .map(Answer::getId)
                .toList();

        List<RetrospectReaction> retrospectReactions = retrospectReactionRepository.findAllByAnswerIdIn(answerIds);

        List<Long> memberIds = retrospectReactions.stream().map(RetrospectReaction::getMemberId).distinct().toList();
        Members members = new Members(memberRepository.findAllById(memberIds));

        Map<Long, List<RetrospectReaction>> reactionsByAnswerId = retrospectReactions.stream()
                .collect(Collectors.groupingBy(RetrospectReaction::getAnswerId));

        List<AnswerReactionGetResponse> answerReactions = answerIds.stream()
                .map(answerId -> AnswerReactionGetResponse.of(
                        answerId,
                        reactionsByAnswerId.getOrDefault(answerId, List.of())
                                .stream()
                                .map(r -> RetrospectReactionElementResponse.of(
                                        r,
                                        members.getName(r.getMemberId()),
                                        members.getProfileImageUrl(r.getMemberId())
                                ))
                                .toList()
                ))
                .toList();

        return RetrospectReactionListGetResponse.from(answerReactions);
    }

    public ReactionListGetResponse getAllReactions() {
        List<ReactionGetResponse> reactions = reactionRepository.findAll()
                .stream()
                .map(ReactionGetResponse::from)
                .toList();

        return ReactionListGetResponse.from(reactions);
    }

    public ReactionListGetResponse getRecentReactions(Long memberId, int limit) {
        List<Long> recentReactionIds = retrospectReactionRepository
                .findRecentDistinctReactionIdsByMemberId(memberId, limit);

        List<Reaction> reactions = reactionRepository.findAllById(recentReactionIds);

        Map<Long, Reaction> reactionMap = reactions.stream()
                .collect(Collectors.toMap(Reaction::getId, r -> r));

        List<ReactionGetResponse> reactionResponses = recentReactionIds.stream()
                .filter(reactionMap::containsKey)
                .map(id -> ReactionGetResponse.from(reactionMap.get(id)))
                .toList();

        return ReactionListGetResponse.from(reactionResponses);
    }
}
