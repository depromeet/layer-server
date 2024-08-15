package org.layer.domain.actionItem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.exception.ActionItemExceptionType;
import org.layer.domain.actionItem.controller.dto.request.ActionItemUpdateRequest;
import org.layer.domain.actionItem.controller.dto.response.MemberActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.RetrospectActionItemResponse;
import org.layer.domain.actionItem.controller.dto.response.SpaceActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.SpaceRetrospectActionItemGetResponse;
import org.layer.domain.actionItem.dto.ActionItemResponse;
import org.layer.domain.actionItem.dto.MemberActionItemResponse;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.exception.ActionItemException;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.NOT_FOUND_MEMBER_SPACE_RELATION;
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

        // order 설정을 위해 회고 아이디로 액션아이템 개수 찾기
        int actionItemCount = actionItemRepository.countByRetrospectId(retrospectId);

        // 액션 아이템 생성
        actionItemRepository.save(ActionItem.builder()
                .retrospectId(retrospectId)
                .spaceId(retrospect.getSpaceId())
                .memberId(memberId)
                .content(content)
                .actionItemOrder(actionItemCount + 1)
                .build());
    }


    //== 스페이스의 액션 아이템 조회 ==//
    public SpaceRetrospectActionItemGetResponse getSpaceActionItemList(Long memberId, Long spaceId) {
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

            // 액션 아이템이 없는 회고는 응답에서 제외
            // TODO: 이부분은 디자인팀에게 질문이 필요. "실행 중"인것만 데이터를 줘야하나, 아니면 실행 중 아님 & 실행 목표가 없는 애들도 다 줘야하나
            if(actionItems.isEmpty()) {
                continue;
            }

            List<ActionItemResponse> actionItemResponses = actionItems.stream()
                    .sorted(Comparator.comparingInt(ActionItem::getActionItemOrder)) // order 순으로 정렬
                    .map(ActionItemResponse::of)
                    .toList();

            RetrospectActionItemResponse responseElement = RetrospectActionItemResponse.builder()
                    .retrospectId(doneRetrospect.getId())
                    .retrospectTitle(doneRetrospect.getTitle())
                    .actionItemList(actionItemResponses)
                    .build();

            response.add(responseElement);
        }

        return SpaceRetrospectActionItemGetResponse.of(space, response);
    }

    @Transactional
    public void deleteActionItem(Long memberId, Long actionItemId) {
        ActionItem actionItem = actionItemRepository.findByIdOrThrow(actionItemId);
        // 액션 아이템을 작성한 사람이 맞는지 확인
        actionItem.isWriter(memberId);
        actionItemRepository.delete(actionItem);
    }

    //== space에서 가장 최근에 끝난 회고에 대한 실행 목표 조회 ==//
    public SpaceActionItemGetResponse getSpaceRecentActionItems(Long memberId, Long spaceId) {
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

            return SpaceActionItemGetResponse.of(space, recent, actionItems);
        }

        // DONE인 회고가 없는 경우
        return SpaceActionItemGetResponse.of(space, null, new ArrayList<>());
    }


    //== 회원의 실행 목표 조회 ==//
    public MemberActionItemGetResponse getMemberActionItemList(Long currentMemberId) {
        // 멤버가 속한 스페이스 모두 가져오기
        List<Space> spaces = spaceRepository.findByMemberId(currentMemberId);

        // 끝난 회고 모두 찾기 (데드라인 내림차순)
        List<Long> spaceIds = spaces.stream().map(Space::getId).toList();
        List<Retrospect> doneRetrospects = retrospectRepository.findAllBySpaceIdIn(spaceIds)
                .stream()
                .filter(retrospect -> retrospect.getRetrospectStatus().equals(DONE))
                .sorted((a, b) -> b.getDeadline().compareTo(a.getDeadline())) // 최근에 끝난 순으로 정렬
                .toList();

        List<MemberActionItemResponse> responses = actionItemRepository
                .findAllMemberActionItemResponses(doneRetrospects)
                .stream()
                .toList();

        for (MemberActionItemResponse response : responses) {
            List<ActionItem> actionItems = actionItemRepository.findAllByRetrospectId(response.getRetrospectId());
            List<ActionItemResponse> actionItemResList = actionItems.stream()
                    .sorted(Comparator.comparingInt(ActionItem::getActionItemOrder)) // order 순으로 정렬
                    .map(ActionItemResponse::of)
                    .toList();
            response.updateActionItemList(actionItemResList);
        }

        return new MemberActionItemGetResponse(responses);
    }

    //== 실행 목표 수정 ==//
    @Transactional
    public void updateActionItems(Long memberId, Long retrospectId, ActionItemUpdateRequest updateDto) {
        // 실행 목표 가져오기
        List<ActionItem> actionItems = actionItemRepository.findAllByRetrospectId(retrospectId);

        // 리더인지 검증
        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        Space space = spaceRepository.findByIdOrThrow(retrospect.getSpaceId());
        space.isLeaderSpace(memberId);


        // O(1) 접근을 위해서 map으로 변경
        Map<Long, ActionItem> actionItemMap = actionItems.stream().collect(Collectors.toMap(
                ActionItem::getId,
                actionItem -> actionItem
        ));

        AtomicInteger order = new AtomicInteger(1);
        for(ActionItemUpdateRequest.ActionItemUpdateElementRequest updateItem : updateDto.actionItems()) {
            ActionItem actionItem = actionItemMap.getOrDefault(updateItem.getId(), null);
            if(actionItem == null) {
                throw new ActionItemException(ActionItemExceptionType.INVALID_ACTION_ITEM_ID);
            }
            actionItem.updateContent(updateItem.getContent());
            actionItem.updateActionItemOrder(order.getAndIncrement());
        }
    }
}
