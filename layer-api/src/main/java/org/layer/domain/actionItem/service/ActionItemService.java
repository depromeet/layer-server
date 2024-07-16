package org.layer.domain.actionItem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.actionItem.dto.CreateActionItemResponse;
import org.layer.domain.actionItem.dto.MemberActionItemResponse;
import org.layer.domain.actionItem.entity.ActionItem;
import org.layer.domain.actionItem.repository.ActionItemRepository;
import org.layer.domain.member.exception.MemberException;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.exception.MemberSpaceRelationException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // FIXME: 삭제
        for (ActionItem actionItem : actionItemList) {
            log.info(actionItem.getContent());
        }

        List<MemberActionItemResponse> memberActionItemList = new ArrayList<>();
        for (ActionItem actionItem : actionItemList) {
            // space 찾기
            Space space = spaceRepository.findByIdOrThrow(actionItem.getSpaceId());

            // 회고 찾기
            Retrospect retrospect = retrospectRepository.findByIdOrThrow(actionItem.getRetrospectId());
            // FIXME: 현재 isTeam을 무조건 true로 넣어놓는 상태 => 팀, 개인 스페이스 구별 필요
            memberActionItemList.add(MemberActionItemResponse.toResponse(actionItem, space.getName(), retrospect.getTitle(), true));
        }

        return memberActionItemList;
    }



}
