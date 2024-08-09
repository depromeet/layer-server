package org.layer.domain.actionItem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.MemberId;
import org.layer.domain.actionItem.controller.dto.CreateActionItemRequest;
import org.layer.domain.actionItem.controller.dto.GetMemberActionItemResponse;
import org.layer.domain.actionItem.controller.dto.GetSpaceActionItemResponse;
import org.layer.domain.actionItem.controller.dto.GetSpaceRetrospectActionItemResponse;
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
                                              @Validated @RequestBody CreateActionItemRequest createActionItemRequest) {
        actionItemService.createActionItem(memberId,
                createActionItemRequest.retrospectId(),
                createActionItemRequest.content());

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/member")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetMemberActionItemResponse> memberActionItem(@MemberId Long currentMemberId) {
        GetMemberActionItemResponse memberActionItems = actionItemService.getMemberActionItemList(currentMemberId);
        return new ResponseEntity<>(memberActionItems, HttpStatus.OK);
    }

    @Override
    @GetMapping("/space/{spaceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetSpaceRetrospectActionItemResponse> teamActionItem(@MemberId Long memberId, @PathVariable(name = "spaceId") Long spaceId) {
        GetSpaceRetrospectActionItemResponse teamActionItem = actionItemService.getSpaceActionItemList(memberId, spaceId);

        return new ResponseEntity<>(teamActionItem, HttpStatus.OK);
    }

    @Override
    @GetMapping("/space/{spaceId}/recent")
    public ResponseEntity<GetSpaceActionItemResponse> spaceRecentActionItem(@MemberId Long memberId, @PathVariable(name = "spaceId") Long spaceId) {
        GetSpaceActionItemResponse spaceRecentActionItems = actionItemService.getSpaceRecentActionItems(memberId, spaceId);

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
