package org.layer.domain.actionItem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.layer.domain.actionItem.controller.dto.CreateActionItemResponse;
import org.layer.domain.actionItem.controller.dto.DeleteActionItemResponse;
import org.layer.domain.actionItem.controller.dto.MemberActionItemResponse;
import org.layer.domain.actionItem.controller.dto.SpaceActionItemElementResponse;
import org.layer.domain.actionItem.controller.dto.SpaceActionItemResponse;
import org.layer.domain.actionItem.dto.*;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.repository.ActionItemRepository;
import org.layer.domain.member.exception.MemberException;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.SpaceCategory;
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

import static org.layer.common.exception.MemberExceptionType.FORBIDDEN;
import static org.layer.common.exception.MemberSpaceRelationExceptionType.NOT_FOUND_MEMBER_SPACE_RELATION;
import static org.layer.domain.actionItem.enums.ActionItemStatus.PROCEEDING;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActionItemService {
    private final ActionItemRepository actionItemRepository;
    private final RetrospectRepository retrospectRepository;
    private final MemberSpaceRelationRepository memberSpaceRelationRepository;
    private final SpaceRepository spaceRepository;

    @Transactional
    public CreateActionItemResponse createActionItem(Long memberId, Long retrospectId, String content) {

        // 멤버가 해당 회고가 진행 중인 스페이스에 속하는지 확인
        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(retrospect.getSpaceId(), retrospectId);

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

        return new CreateActionItemResponse(memberId, retrospect.getSpaceId());
    }

    public List<MemberActionItemResponse> getMemberActionItemList(Long currentMemberId, Long memberId) {
        // 현재 로그인된 회원과 액션 아이템 리스트의 주인 회원이 일치하지 않음
        if(!memberId.equals(currentMemberId)) {
            throw new MemberException(FORBIDDEN);
        }

        List<ActionItem> actionItemList = actionItemRepository.findAllByMemberIdAndActionItemStatusOrderByCreatedAtDesc(memberId, PROCEEDING);

        return processMemberActionItems(actionItemList);
    }

    public SpaceActionItemResponse getSpaceActionItemList(Long memberId, Long spaceId) {
        // 현재 로그인한 회원이 회고 스페이스에 속하는지 확인
        Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId);
        if(team.isEmpty()) {
            throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
        }

        List<ActionItem> actionItemList = actionItemRepository.findAllBySpaceIdAndActionItemStatusOrderByCreatedAtDesc(spaceId, PROCEEDING);

        // IN절로 수정
        List<SpaceActionItemElementResponse> teamActionItemList = processSpaceActionItems(actionItemList);

        // space 찾기
        Space space = spaceRepository.findByIdOrThrow(spaceId);
        
        return SpaceActionItemResponse.builder()
                .spaceId(spaceId)
                .spaceName(space.getName())
                .teamActionItemList(teamActionItemList)
                .build();
    }

    @Transactional
    public DeleteActionItemResponse deleteActionItem(Long memberId, Long actionItemId) {
        ActionItem actionItem = actionItemRepository.findByIdOrThrow(actionItemId);

        // 액션 아이템을 작성한 사람이 맞는지 확인
        actionItem.isWriter(memberId);

        actionItemRepository.delete(actionItem);

        return new DeleteActionItemResponse(actionItemId);
    }



    // getMemberActionItemList 메서드에서 사용 (쿼리 IN절로 수정 로직)
    private List<MemberActionItemResponse> processMemberActionItems(List<ActionItem> actionItemList) {
        List<MemberActionItemResponse> memberActionItemList = new ArrayList<>();
        List<Long> spaceIds = actionItemList.stream()
                .map(ActionItem::getSpaceId)
                .collect(Collectors.toList());

        List<Long> retrospectIds = actionItemList.stream()
                .map(ActionItem::getRetrospectId)
                .collect(Collectors.toList());

        // 스페이스, 회고 정보 가져오기
        List<Space> spaces = spaceRepository.findByIdIn(spaceIds);
        List<Retrospect> retrospects = retrospectRepository.findByIdIn(retrospectIds);

        // Map으로 매핑
        Map<Long, Space> spaceMap = spaces.stream()
                .collect(Collectors.toMap(
                        Space::getId,
                        space -> space
                ));

        Map<Long, Retrospect> retrospectMap = retrospects.stream()
                .collect(Collectors.toMap(
                        Retrospect::getId,
                        retrospect -> retrospect
                ));


        // 회원의 액션 아이템 리스트에 추가
        for (ActionItem actionItem : actionItemList) {
            Space space = spaceMap.get(actionItem.getSpaceId());
            Retrospect retrospect = retrospectMap.get(actionItem.getRetrospectId());

            // TODO: 이런 상황에서 어떻게 할지 좀 더 고민 필요
            if (space == null || retrospect == null) {
                continue; // 회고 또는 스페이스가 삭제되고 액션 아이템이 남은 상황
            }

            boolean isTeam = space.getCategory().equals(SpaceCategory.TEAM);
            memberActionItemList.add(MemberActionItemResponse.toResponse(actionItem, space.getName(), retrospect.getTitle(), isTeam));
        }

        return memberActionItemList;
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
                    .retrospectName(retrospect.getTitle())
                    .actionItemContent(actionItem.getContent())
                    .build());
        }

        return spaceActionItemList;
    }



}
