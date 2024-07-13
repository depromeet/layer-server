package org.layer.domain.space.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.dto.Meta;
import org.layer.common.exception.BaseCustomException;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.layer.domain.space.exception.SpaceExceptionType.SPACE_NOT_FOUND;

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


        var spaceList = spacePages.stream().map(SpaceResponse.SpaceWithUserCountInfo::toResponse).collect(Collectors.toList());

        var meta = Meta.builder().cursor(newCursor).hasNextPage(hasNextPage).build();
        return SpaceResponse.SpacePage.toResponse(spaceList, meta);
    }

    @Transactional
    public void createSpace(Long memberId, SpaceRequest.CreateSpaceRequest mutateSpaceRequest) {
        var newSpace = spaceRepository.save(mutateSpaceRequest.toEntity(memberId));
        var memberSpaceRelation = MemberSpaceRelation.builder().memberId(memberId).spaceId(newSpace.getId()).build();

        memberSpaceRelationRepository.save(memberSpaceRelation);
    }

    @Transactional
    public void updateSpace(Long memberId, SpaceRequest.UpdateSpaceRequest updateSpaceRequest) {
        spaceRepository.findByIdAndJoinedMemberId(updateSpaceRequest.id(), memberId).orElseThrow(() -> new BaseCustomException(SPACE_NOT_FOUND));
        spaceRepository.updateSpace(updateSpaceRequest.id(), updateSpaceRequest.category(), updateSpaceRequest.field(), updateSpaceRequest.name(), updateSpaceRequest.introduction());
    }

    public SpaceResponse.SpaceWithUserCountInfo getSpaceById(Long memberId, Long spaceId) {
        var foundSpace = spaceRepository.findByIdAndJoinedMemberId(spaceId, memberId).orElseThrow(() -> new BaseCustomException(SPACE_NOT_FOUND));

        return SpaceResponse.SpaceWithUserCountInfo.toResponse(foundSpace);
    }


}

//https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=ad070effc980074c24bde76fd1931fba&redirect_uri=http://localhost:8080/oauth/kakao