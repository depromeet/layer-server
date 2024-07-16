package org.layer.domain.actionItem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.MemberId;
import org.layer.domain.actionItem.api.ActionItemApi;
import org.layer.domain.actionItem.dto.*;
import org.layer.domain.actionItem.service.ActionItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/action-item")
@RestController
public class ActionItemController implements ActionItemApi {
    private final ActionItemService actionItemService;
    @Override
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateActionItemResponse> createActionItem(@MemberId Long memberId,
                                                                     @Validated @RequestBody CreateActionItemRequest createActionItemRequest) {
        CreateActionItemResponse actionItem = actionItemService.createActionItem(memberId,
                createActionItemRequest.retrospectId(),
                createActionItemRequest.content());

        return new ResponseEntity<>(actionItem, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/member/{memberId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MemberActionItemResponse>> memberActionItem(@MemberId Long currentMemberId, @PathVariable("memberId") Long memberId) {
        List<MemberActionItemResponse> memberActionItemList = actionItemService.getMemberActionItemList(currentMemberId, memberId);

        return new ResponseEntity<>(memberActionItemList, HttpStatus.OK);
    }

    @Override
    @GetMapping("/space/{spaceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamActionItemResponse> teamActionItem(@MemberId Long memberId, @PathVariable("spaceId") Long spaceId) {
        TeamActionItemResponse teamActionItem = actionItemService.getTeamActionItemList(memberId, spaceId);

        return new ResponseEntity<>(teamActionItem, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{actionItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DeleteActionItemResponse> deleteActionItem(@MemberId Long memberId, @PathVariable("actionItemId") Long actionItemId) {
        DeleteActionItemResponse deleteActionItemResponse = actionItemService.deleteActionItem(memberId, actionItemId);

        return new ResponseEntity<>(deleteActionItemResponse, HttpStatus.OK);
    }
}
