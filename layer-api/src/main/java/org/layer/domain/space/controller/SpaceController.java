package org.layer.domain.space.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.MemberId;
import org.layer.domain.space.api.SpaceApi;
import org.layer.domain.space.dto.SpaceRequest;
import org.layer.domain.space.dto.SpaceResponse;
import org.layer.domain.space.service.SpaceService;
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
    public ResponseEntity<SpaceResponse.SpacePage> getMySpaceList(@MemberId Long memberId, SpaceRequest.GetSpaceRequest getSpaceRequest) {
        var response = spaceService.getSpaceListFromMemberId(memberId, getSpaceRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> createSpace(@MemberId Long memberId, @RequestBody @Validated SpaceRequest.CreateSpaceRequest createSpaceRequest) {
        var response = spaceService.createSpace(memberId, createSpaceRequest);
        return ResponseEntity.ok(response);
    }
}
