package org.layer.domain.space.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.dto.Meta;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.exception.SpaceException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.layer.domain.space.exception.SpaceExceptionType.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final MemberSpaceRelationRepository memberSpaceRelationRepository;

    public SpaceResponse.SpacePage getSpaceListFromMemberId(Long memberId, SpaceRequest.GetSpaceRequest getSpaceRequest) {

        var spacePages = spaceRepository.findAllSpacesByMemberIdAndCategoryAndCursor(memberId, getSpaceRequest.cursorId(), getSpaceRequest.category(), getSpaceRequest.pageSize());

        boolean hasNextPage = spacePages.size() > getSpaceRequest.pageSize();
        if (hasNextPage) {
            spacePages.remove(spacePages.size() - 1);
        }
        Long newCursor = !hasNextPage ? null : spacePages.isEmpty() ? null : spacePages.get(spacePages.size() - 1).getId();


        var spaceList = spacePages.stream().map(SpaceResponse.SpaceWithMemberCountInfo::toResponse).collect(Collectors.toList());

        var meta = Meta.builder().cursor(newCursor).hasNextPage(hasNextPage).build();
        return SpaceResponse.SpacePage.toResponse(spaceList, meta);
    }

    @Transactional
    public void createSpace(Long memberId, SpaceRequest.CreateSpaceRequest mutateSpaceRequest) {
        var newSpace = spaceRepository.save(mutateSpaceRequest.toEntity(memberId));
        var memberSpaceRelation = MemberSpaceRelation.builder().memberId(memberId).space(newSpace).build();

        memberSpaceRelationRepository.save(memberSpaceRelation);
    }

    @Transactional
    public void updateSpace(Long memberId, SpaceRequest.UpdateSpaceRequest updateSpaceRequest) {
        spaceRepository.findByIdAndJoinedMemberId(updateSpaceRequest.id(), memberId).orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));
        spaceRepository.updateSpace(updateSpaceRequest.id(), updateSpaceRequest.category(), updateSpaceRequest.fieldList(), updateSpaceRequest.name(), updateSpaceRequest.introduction(), updateSpaceRequest.bannerUrl());
    }

    public SpaceResponse.SpaceWithMemberCountInfo getSpaceById(Long memberId, Long spaceId) {
        var foundSpace = spaceRepository.findByIdAndJoinedMemberId(spaceId, memberId).orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        return SpaceResponse.SpaceWithMemberCountInfo.toResponse(foundSpace);
    }

    @Transactional
    public void createMemberSpace(Long memberId, Long spaceId) {

        /*
          존재하는 스페이스 여부 확인
         */
        var foundSpace = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND)
                );

        if (foundSpace.getCategory() == SpaceCategory.INDIVIDUAL) {
            throw new SpaceException(SPACE_NOT_FOUND);
        }


        /*
          이미 참여중인 스페이스 여부 확인
         */
        memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId).ifPresent(it -> {
            throw new SpaceException(SPACE_ALREADY_JOINED);
        });

        /*
          스페이스 참여여부 저장
         */
        var joinedSpace = MemberSpaceRelation.builder()
                .space(foundSpace)
                .memberId(memberId)
                .build();
        memberSpaceRelationRepository.save(joinedSpace);
    }

    @Transactional
    public void removeMemberSpace(Long memberId, Long spaceId) {
        /*
          존재하는 스페이스 여부 확인
         */
        var foundSpace = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND)
                );

        /*
          스페이스 팀장 여부 확인
         */
        foundSpace.isLeaderSpace(memberId)
                .orElseThrow(() -> new SpaceException(SPACE_LEADER_CANNOT_LEAVE));

        /*
          개인 스페이스의 경우, 이탈 시 Space 엔티티의 로직이 된다.
          따라서 INDIVIDUAL인 Space인 경우 가능한 케이스는 2가지로
          1. Space 삭제
          2. 속한 스페이스 떠나기, 스페이스 삭제하기 분리
          현재는 2번 케이스를 기준으로 구현되어 있음
         */
        if (foundSpace.getCategory() == SpaceCategory.INDIVIDUAL) {
            throw new SpaceException(SPACE_NOT_FOUND);
        }

        /*
          이미 참여중인 스페이스 여부 확인
         */
        var foundMemberSpaceRelation = memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId)
                .orElseThrow(
                        () -> new SpaceException(SPACE_ALREADY_JOINED)
                );

        /*
          스페이스 참여여부 삭제 ( 스페이스 떠나기 )
         */
        memberSpaceRelationRepository.deleteById(foundMemberSpaceRelation.getId());
    }

}
