package org.layer.domain.space.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.annotation.MemberId;
import org.layer.domain.space.controller.dto.SpaceRequest;
import org.layer.domain.space.controller.dto.SpaceResponse;
import org.layer.domain.space.service.SpaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/space")
public class SpaceController implements SpaceApi {

    private final SpaceService spaceService;

    @Override
    @GetMapping("/list")
    public ResponseEntity<SpaceResponse.SpacePage> getMySpaceList(@MemberId Long memberId, @ModelAttribute @Valid SpaceRequest.GetSpaceRequest getSpaceRequest) {
        var response = spaceService.getSpaceListFromMemberId(memberId, getSpaceRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("")
    public ResponseEntity<SpaceResponse.SpaceCreateResponse> createSpace(@MemberId Long memberId, @RequestBody @Valid SpaceRequest.CreateSpaceRequest createSpaceRequest) {
        var newSpaceId = spaceService.createSpace(memberId, createSpaceRequest);

        return ResponseEntity.ok(SpaceResponse.SpaceCreateResponse.builder().spaceId(newSpaceId).build());
    }

    @Override
    @PutMapping("")
    public ResponseEntity<Void> updateSpace(@MemberId Long memberId, @RequestBody @Valid SpaceRequest.UpdateSpaceRequest updateSpaceRequest) {
        spaceService.updateSpace(memberId, updateSpaceRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{spaceId}")
    public ResponseEntity<SpaceResponse.SpaceWithMemberCountInfo> getSpaceById(@MemberId Long memberId, @PathVariable Long spaceId) {
        var foundSpace = spaceService.getSpaceById(memberId, spaceId);
        return ResponseEntity.ok((foundSpace));
    }

    @Override
    @GetMapping("/public/{spaceId}")
    public ResponseEntity<SpaceResponse.SpaceWithMemberCountInfo> getPublicSpaceById(@PathVariable Long spaceId) {
        var foundSpace = spaceService.getPublicSpaceById(spaceId);
        return ResponseEntity.ok((foundSpace));
    }

    @Override
    @PostMapping("/join")
    public ResponseEntity<Void> createMemberSpace(@MemberId Long memberId, @RequestParam Long spaceId) {
        spaceService.createMemberSpace(memberId, spaceId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/leave")
    public ResponseEntity<Void> removeMemberSpace(@MemberId Long memberId, @RequestBody @Valid SpaceRequest.LeaveSpaceRequest leaveSpaceRequest) {
        spaceService.removeMemberSpace(memberId, leaveSpaceRequest.spaceId());
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/change-leader")
    public ResponseEntity<Void> changeSpaceLeader(@MemberId Long memberId, @RequestBody @Valid SpaceRequest.ChangeSpaceLeaderRequest changeSpaceLeaderRequest) {
        spaceService.changeSpaceLeader(memberId, changeSpaceLeaderRequest.spaceId(), changeSpaceLeaderRequest.memberId());
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/kick")
    public ResponseEntity<Void> kickMemberFromSpace(@MemberId Long memberId, @RequestBody @Valid SpaceRequest.KickMemberFromSpaceRequest kickMemberFromSpaceRequest) {
        spaceService.kickMemberFromSpace(memberId, kickMemberFromSpaceRequest.spaceId(), kickMemberFromSpaceRequest.memberId());
        return ResponseEntity.ok().build();

    }

    @Override
    @GetMapping("/members/{spaceId}")
    public ResponseEntity<List<SpaceResponse.SpaceMemberResponse>> getSpaceMembers(@MemberId Long memberId, @PathVariable Long spaceId) {
        var spaceMembers = spaceService.getSpaceMembers(memberId, spaceId);
        return ResponseEntity.ok(spaceMembers);
    }

    @Override
    @DeleteMapping("/{spaceId}")
    public ResponseEntity<Void> removeSpace(@MemberId Long memberId, @PathVariable("spaceId") Long spaceId) {
        spaceService.removeSpace(spaceId, memberId);
        return ResponseEntity.ok().build();
    }
}

