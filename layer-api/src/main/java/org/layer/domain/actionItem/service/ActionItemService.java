package org.layer.domain.actionItem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static org.layer.common.exception.ActionItemExceptionType.*;
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

        // 만드는 사람이 스페이스 리더인지 확인
        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        Space space = spaceRepository.findByIdOrThrow(retrospect.getSpaceId());
        space.isLeaderSpace(memberId);


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

    @Transactional
    public void createActionItemBySpaceId(Long memberId, Long spaceId, String content) {
        // 만드는 사람이 스페이스 리더인지 확인
        Space space = spaceRepository.findByIdOrThrow(spaceId);
        space.isLeaderSpace(memberId);

        // 가장 최근 실행 목표 찾기
        Optional<Retrospect> retrospectOpt = retrospectRepository.findAllBySpaceId(spaceId)
                .stream()
                .filter(retrospect -> retrospect.getRetrospectStatus().equals(DONE))
                .sorted((a, b) -> b.getDeadline().compareTo(a.getDeadline()))
                .findFirst();

        if(retrospectOpt.isEmpty()) { // "실행 중" 회고가 존재하지 않음
            throw new ActionItemException(NO_PROCEEDING_ACTION_ITEMS);
        }

        Retrospect retrospect = retrospectOpt.get();

        // order 설정을 위해 회고 아이디로 실행 목표 개수 찾기
        int actionItemCount = actionItemRepository.countByRetrospectId(retrospect.getId());

        // 액션 아이템 생성
        actionItemRepository.save(ActionItem.builder()
                .retrospectId(retrospect.getId())
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

        List<Long> doneRetrospectIds = doneRetrospects.stream().map(Retrospect::getId).toList();
        List<ActionItem> actionItemList = actionItemRepository.findAllByRetrospectIdIn(doneRetrospectIds);

        List<RetrospectActionItemResponse> responses = new ArrayList<>();
        for (Retrospect doneRetrospect : doneRetrospects) {

            List<ActionItemResponse> actionItems = actionItemList.stream()
                    .filter(ai -> ai.getRetrospectId().equals(doneRetrospect.getId()))
                    .sorted(Comparator.comparingInt(ActionItem::getActionItemOrder)) // order순 정렬
                    .map(ActionItemResponse::of).toList();

            RetrospectActionItemResponse response = RetrospectActionItemResponse.builder()
                    .retrospectId(doneRetrospect.getId())
                    .retrospectTitle(doneRetrospect.getTitle())
                    .actionItemList(actionItems)
                    .build();

            responses.add(response);
        }




        return SpaceRetrospectActionItemGetResponse.of(space, responses);
    }

    @Transactional
    public void deleteActionItem(Long memberId, Long actionItemId) {
        ActionItem actionItem = actionItemRepository.findByIdOrThrow(actionItemId);
        Space space = spaceRepository.findByIdOrThrow(actionItem.getSpaceId());

        // 지우는 사람이 space leader인지 확인
        space.isLeaderSpace(memberId);

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
            List<ActionItem> actionItems = actionItemRepository
                    .findAllByRetrospectId(recent.getId()).stream()
                    .sorted(Comparator.comparingInt(ActionItem::getActionItemOrder)) // order 순으로 정렬
                    .toList();

            return SpaceActionItemGetResponse.of(space, recent, actionItems);
        }

        // DONE인 회고가 없는 경우
        return SpaceActionItemGetResponse.of(space, null, new ArrayList<>());
    }


    //== 회원의 실행 목표 조회 ==//
    public MemberActionItemGetResponse getMemberActionItemList(Long currentMemberId) {
        // 멤버가 속한 스페이스 정보와 회고 모두 가져오기 (회고 데드라인 내림차순)
        List<MemberActionItemResponse> dtoList = retrospectRepository.findAllMemberActionItemResponsesByMemberId(currentMemberId);

        List<Long> doneRetrospectIds = dtoList.stream()
                .map(MemberActionItemResponse::getRetrospectId)
                .toList();

        // 실행 목표 모두 찾기
        List<ActionItem> actionItemList = actionItemRepository.findAllByRetrospectIdIn(doneRetrospectIds);

        for(MemberActionItemResponse dto : dtoList) {
            List<ActionItemResponse> actionItems = actionItemList.stream()
                    .filter(ai -> ai.getRetrospectId().equals(dto.getRetrospectId()))
                    .sorted(Comparator.comparingInt(ActionItem::getActionItemOrder)) // order 순으로 정렬
                    .map(ActionItemResponse::of).toList();

            dto.updateActionItemList(actionItems);
        }

        return new MemberActionItemGetResponse(dtoList);
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

        // 요청 리스트와 DB에 저장된 실행 목표 개수가 다를 때
        if(updateDto.actionItems().size() != actionItems.size()) {
            throw new ActionItemException(INVALID_ACTION_ITEM_LIST);
        }


        // O(1) 접근을 위해서 map으로 변경
        Map<Long, ActionItem> actionItemMap = actionItems.stream().collect(Collectors.toMap(
                ActionItem::getId,
                actionItem -> actionItem
        ));

        AtomicInteger order = new AtomicInteger(1);
        for(ActionItemUpdateRequest.ActionItemUpdateElementRequest updateItem : updateDto.actionItems()) {
            ActionItem actionItem = actionItemMap.getOrDefault(updateItem.getId(), null);
            if(actionItem == null) {
                throw new ActionItemException(INVALID_ACTION_ITEM_ID);
            }
            actionItem.updateContent(updateItem.getContent());
            actionItem.updateActionItemOrder(order.getAndIncrement());
        }
    }
}
