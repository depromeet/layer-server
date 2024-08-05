package org.layer.domain.actionItem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.actionItem.controller.dto.*;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.repository.ActionItemRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.MemberSpaceRelationException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.NOT_FOUND_MEMBER_SPACE_RELATION;
import static org.layer.domain.actionItem.enums.ActionItemStatus.PROCEEDING;
import static org.layer.domain.retrospect.entity.RetrospectStatus.DONE;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ActionItemService {
    private final ActionItemRepository actionItemRepository;
    private final RetrospectRepository retrospectRepository;
    private final MemberSpaceRelationRepository memberSpaceRelationRepository;
    private final SpaceRepository spaceRepository;

    @Transactional
    public void createActionItem(Long memberId, Long retrospectId, String content) {

        // 멤버가 해당 회고가 진행 중인 스페이스에 속하는지 확인
        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(retrospect.getSpaceId(), memberId);

        if(team.isEmpty()) {
            throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
        }

        // 액션 아이템 생성
        actionItemRepository.save(ActionItem.builder()
                .retrospectId(retrospectId)
                .spaceId(retrospect.getSpaceId())
                .memberId(memberId)
                .content(content)
                .actionItemStatus(PROCEEDING)
                .build());
    }


    public GetSpaceRetrospectActionItemResponse getSpaceActionItemList(Long memberId, Long spaceId) {
        // space가 존재하는지 확인
        Space space = spaceRepository.findByIdOrThrow(spaceId);

        // 현재 로그인한 회원이 회고 스페이스에 속하는지 확인
        Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId);
        if(team.isEmpty()) {
            throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
        }

        // 끝난 회고 모두 찾기
        List<Retrospect> doneRetrospects = retrospectRepository.findAllBySpaceId(spaceId)
                .stream()
                .filter(retrospect -> retrospect.getRetrospectStatus().equals(DONE))
                .sorted((a, b) -> b.getDeadline().compareTo(a.getDeadline()))
                .toList();

        List<RetrospectActionItemResponse> response = new ArrayList<>();
        for (Retrospect doneRetrospect : doneRetrospects) {
            List<ActionItem> actionItems = actionItemRepository.findAllByRetrospectId(doneRetrospect.getId());
            List<ActionItemResponse> actionItemResponses = actionItems.stream()
                    .map(ActionItemResponse::of)
                    .toList();

            RetrospectActionItemResponse responseElement = RetrospectActionItemResponse.builder()
                    .retrospectId(doneRetrospect.getId())
                    .retrospectTitle(doneRetrospect.getTitle())
                    .actionItemList(actionItemResponses)
                    .build();

            response.add(responseElement);
        }

        return GetSpaceRetrospectActionItemResponse.of(space, response);
    }

    @Transactional
    public void deleteActionItem(Long memberId, Long actionItemId) {
        ActionItem actionItem = actionItemRepository.findByIdOrThrow(actionItemId);
        // 액션 아이템을 작성한 사람이 맞는지 확인
        actionItem.isWriter(memberId);
        actionItemRepository.delete(actionItem);
    }

    // space에서 모든 끝난 회고에 대한 실행 목표 조회
    public GetSpaceActionItemResponse getSpaceRecentActionItems(Long memberId, Long spaceId) {
        // 스페이스가 있는지 검증
        Space space = spaceRepository.findByIdOrThrow(spaceId);
        
        // 멤버가 스페이스에 속하는지 검증
        memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId);

        List<Retrospect> retrospectList = retrospectRepository.findAllBySpaceId(spaceId);

        // 종료된 것 중 가장 최근에 만들어진 회고 찾기
        Optional<Retrospect> recentOpt = retrospectList.stream()
                .filter(r -> r.getRetrospectStatus().equals(DONE)) // 끝난 회고 찾기
                .sorted((a, b) -> b.getDeadline().compareTo(a.getDeadline())) // deadline 내림차순으로 정렬
                .findFirst();

        if(recentOpt.isPresent()) {
            Retrospect recent = recentOpt.get();
            List<ActionItem> actionItems = actionItemRepository.findAllByRetrospectId(recent.getId());
            return GetSpaceActionItemResponse.of(space, recent, actionItems);
        }
        return null;
    }


    public MemberActionItemResponse getMemberActionItemList(Long currentMemberId) {
        // 멤버가 속한 스페이스 모두 가져오기
        List<MemberSpaceRelation> memberSpaceRelations = memberSpaceRelationRepository.findAllByMemberId(currentMemberId);
        List<Space> spaces = memberSpaceRelations.stream().map(MemberSpaceRelation::getSpace).toList(); // TODO: N+1 쿼리 나갈것 같은데.. 함 봐야겠다

        // 스페이스에서 상태가 Done인 회고 모두 가져오기
        List<Long> spaceIds = spaces.stream().map(Space::getId).toList();
        List<Retrospect> doneRetrospects = retrospectRepository.findAllBySpaceIdIn(spaceIds).stream()
                .filter(s -> s.getRetrospectStatus().equals(DONE))
                .toList();

        // 액션 아이템 모두 뽑아오기
        List<Long> idList = doneRetrospects.stream().map(Retrospect::getId).toList();
        List<ActionItem> actionItemList = actionItemRepository.findAllByRetrospectIdIn(idList).stream()
                .sorted((a, b) -> {
                    if(a.getIsPinned() && b.getIsPinned()
                            && a.getActionItemStatus().equals(b.getActionItemStatus())) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt()); // 둘다 핀 돼있고, 상태도 같으면 최신 순
                    } else if(a.getIsPinned() && b.getIsPinned()) {
                        return a.getActionItemStatus().getPriority() - b.getActionItemStatus().getPriority();
                    } else if(a.getIsPinned() || b.getIsPinned()) {
                        return a.getIsPinned() ? -1 : 1;
                    } else if(!a.getActionItemStatus().equals(b.getActionItemStatus())) {
                        return a.getActionItemStatus().getPriority() - b.getActionItemStatus().getPriority();
                    } else {
                        return b.getCreatedAt().compareTo(a.getCreatedAt()); // 둘다 핀 돼있고, proceeding 이면 최신 순
                    }
                }).toList();

        List<MemberActionItemElementResponse> response = new ArrayList<>();
        for (ActionItem actionItem : actionItemList) {
            Space space = spaceRepository.findByIdOrThrow(actionItem.getSpaceId());
            Retrospect retrospect = retrospectRepository.findByIdOrThrow(actionItem.getRetrospectId());

            MemberActionItemElementResponse responseElement = MemberActionItemElementResponse.of(space, retrospect, actionItem);
            response.add(responseElement);
        }

        return new MemberActionItemResponse(response);
    }
}
