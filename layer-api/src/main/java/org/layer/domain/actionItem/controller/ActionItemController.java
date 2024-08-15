package org.layer.domain.actionItem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.MemberId;
import org.layer.domain.actionItem.controller.dto.request.ActionItemCreateRequest;
import org.layer.domain.actionItem.controller.dto.response.MemberActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.SpaceActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.SpaceRetrospectActionItemGetResponse;
import org.layer.domain.actionItem.service.ActionItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/action-item")
@RestController
public class ActionItemController implements ActionItemApi {
    private final ActionItemService actionItemService;
    @Override
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> createActionItem(@MemberId Long memberId,
                                              @Validated @RequestBody ActionItemCreateRequest actionItemCreateRequest) {
        actionItemService.createActionItem(memberId,
                actionItemCreateRequest.retrospectId(),
                actionItemCreateRequest.content());

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/member")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberActionItemGetResponse> memberActionItem(@MemberId Long currentMemberId) {
        MemberActionItemGetResponse memberActionItems = actionItemService.getMemberActionItemList(currentMemberId);
        return new ResponseEntity<>(memberActionItems, HttpStatus.OK);
    }

    @Override
    @GetMapping("/space/{spaceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SpaceRetrospectActionItemGetResponse> teamActionItem(@MemberId Long memberId, @PathVariable(name = "spaceId") Long spaceId) {
        SpaceRetrospectActionItemGetResponse teamActionItem = actionItemService.getSpaceActionItemList(memberId, spaceId);

        return new ResponseEntity<>(teamActionItem, HttpStatus.OK);
    }

    @Override
    @GetMapping("/space/{spaceId}/recent")
    public ResponseEntity<SpaceActionItemGetResponse> spaceRecentActionItem(@MemberId Long memberId, @PathVariable(name = "spaceId") Long spaceId) {
        SpaceActionItemGetResponse spaceRecentActionItems = actionItemService.getSpaceRecentActionItems(memberId, spaceId);

        return new ResponseEntity<>(spaceRecentActionItems, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{actionItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteActionItem(@MemberId Long memberId, @PathVariable("actionItemId") Long actionItemId) {
        actionItemService.deleteActionItem(memberId, actionItemId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
