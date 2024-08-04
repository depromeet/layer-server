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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.NOT_FOUND_MEMBER_SPACE_RELATION;
import static org.layer.domain.actionItem.enums.ActionItemStatus.PROCEEDING;
import static org.layer.domain.retrospect.entity.RetrospectStatus.DONE;

@Slf4j
@RequiredArgsConstructor
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


    public SpaceRetrospectActionItemResponse getSpaceActionItemList(Long memberId, Long spaceId) {
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

        return SpaceRetrospectActionItemResponse.of(space, response);
    }

    @Transactional
    public void deleteActionItem(Long memberId, Long actionItemId) {
        ActionItem actionItem = actionItemRepository.findByIdOrThrow(actionItemId);
        // 액션 아이템을 작성한 사람이 맞는지 확인
        actionItem.isWriter(memberId);
        actionItemRepository.delete(actionItem);
    }

    // getMemberActionItemList 메서드에서 사용 (쿼리 IN절로 수정 로직)
    private List<SpaceActionItemElementResponse> processSpaceActionItems(List<ActionItem> actionItemList) {
        List<SpaceActionItemElementResponse> spaceActionItemList = new ArrayList<>();

        // 회고 아이디 추출
        List<Long> retrospectIds = actionItemList.stream()
                .map(ActionItem::getRetrospectId)
                .collect(Collectors.toList());

        // 회고 정보 가져오기
        List<Retrospect> retrospects = retrospectRepository.findByIdIn(retrospectIds);

        // Map으로 매핑
        Map<Long, Retrospect> retrospectMap = retrospects.stream()
                .collect(Collectors.toMap(
                        Retrospect::getId,
                        retrospect -> retrospect
                ));


        // 스페이스 액션 아이템 리스트에 추가
        for (ActionItem actionItem : actionItemList) {
            Retrospect retrospect = retrospectMap.get(actionItem.getRetrospectId());

            // TODO: 이런 상황에서 어떻게 할지 좀 더 고민 필요
            if (retrospect == null) {
                continue; // 회고가 삭제되고 액션 아이템이 남은 상황
            }

            spaceActionItemList.add(SpaceActionItemElementResponse.builder()
                    .actionItemId(actionItem.getId())
                    .retrospectTitle(retrospect.getTitle())
                    .content(actionItem.getContent())
                    .build());
        }

        return spaceActionItemList;
    }

    // space에서 모든 끝난 회고에 대한 실행 목표 조회
    public SpaceActionItemResponse getSpaceRecentActionItems(Long memberId, Long spaceId) {
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
            return SpaceActionItemResponse.of(space, recent, actionItems);
        }
        return null;
    }
}
