package org.layer.domain.space.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.MemberId;
import org.layer.domain.space.api.SpaceApi;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.layer.domain.space.service.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/space")
public class SpaceController implements SpaceApi {

    private final SpaceService spaceService;

    @Override
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SpaceResponse.SpacePage> getMySpaceList(@MemberId Long memberId, @ModelAttribute @Validated SpaceRequest.GetSpaceRequest getSpaceRequest) {
        var response = spaceService.getSpaceListFromMemberId(memberId, getSpaceRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/")
    @PreAuthorize("isAuthenticated()")
    public void createSpace(@MemberId Long memberId, @RequestBody @Validated SpaceRequest.CreateSpaceRequest createSpaceRequest) {
        spaceService.createSpace(memberId, createSpaceRequest);
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateSpace(@MemberId Long memberId, @RequestBody @Validated SpaceRequest.UpdateSpaceRequest updateSpaceRequest) {
        spaceService.updateSpace(memberId, updateSpaceRequest);
    }

    @GetMapping("/{spaceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SpaceResponse.SpaceWithUserCountInfo> getSpaceById(@MemberId Long memberId, @PathVariable Long spaceId) {
        var foundSpace = spaceService.getSpaceById(memberId, spaceId);
        return ResponseEntity.ok((foundSpace));
    }
}

