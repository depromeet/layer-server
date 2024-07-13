package org.layer.domain.space.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.layer.common.dto.Meta;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final MemberSpaceRelationRepository memberSpaceRelationRepository;

    public SpaceResponse.SpacePage getSpaceListFromMemberId(Long memberId, SpaceRequest.GetSpaceRequest getSpaceRequest) {
        PageRequest pageRequest = PageRequest.of(0, getSpaceRequest.pageSize());
        Page<SpaceWithMemberCount> spacePages;
        if (getSpaceRequest.category().isPresent()) {
            spacePages = spaceRepository.findAllSpacesByMemberIdAndCategoryAndCursor(memberId, getSpaceRequest.cursorId(), getSpaceRequest.category().get(), pageRequest);
        } else {
            spacePages = spaceRepository.findAllSpacesByMemberIdAndCursor(memberId, getSpaceRequest.cursorId(), pageRequest);
        }

        var spaceList = spacePages.stream().map(SpaceResponse.SpaceWithUserCountInfo::toResponse).collect(Collectors.toList());
        boolean hasNextPage = spacePages.hasNext();
        Long newCursor = hasNextPage ? spacePages.getContent().get(spacePages.getNumberOfElements() - 1).getId() : null;

        var meta = Meta.builder().cursor(newCursor).hasNextPage(hasNextPage).build();
        return SpaceResponse.SpacePage.toResponse(spaceList, meta);
    }

    @Transactional
    public Boolean createSpace(Long memberId, SpaceRequest.CreateSpaceRequest createSpaceRequest) {
        Space newSpace = createSpaceRequest.toEntity(memberId);
        var memberSpaceRelation = MemberSpaceRelation.builder().memberId(memberId).spaceId(newSpace.getId()).build();
        spaceRepository.save(newSpace);
        memberSpaceRelationRepository.save(memberSpaceRelation);
        return true;
    }

}
