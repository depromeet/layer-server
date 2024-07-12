package org.layer.domain.space.service;

import lombok.RequiredArgsConstructor;
import org.layer.common.dto.Meta;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.repository.SpaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final SpaceRepository spaceRepository;

    public SpaceResponse.SpacePage getSpaceListFromMemberId(Long memberId, SpaceRequest.GetSpaceRequest getSpaceRequest) {
        PageRequest pageRequest = PageRequest.of(0, getSpaceRequest.pageSize());
        Page<Space> spacePages;
        if (getSpaceRequest.category().isPresent()) {
            spacePages = spaceRepository.findAllSpacesByMemberIdAndCategoryAndCursor(memberId, getSpaceRequest.cursorId(), getSpaceRequest.category().get(), pageRequest);
        } else {
            spacePages = spaceRepository.findAllSpacesByMemberIdAndCursor(memberId, getSpaceRequest.cursorId(), pageRequest);
        }

        var spaceList = spacePages.stream().map(SpaceResponse.SpaceInfo::toResponse).collect(Collectors.toList());
        boolean hasNextPage = spacePages.hasNext();
        Long newCursor = hasNextPage ? spacePages.getContent().get(spacePages.getNumberOfElements() - 1).getId() : null;

        var meta = Meta.builder().cursor(newCursor).hasNextPage(hasNextPage).build();
        return SpaceResponse.SpacePage.toResponse(spaceList, meta);
    }

}
