package org.layer.domain.actionItem.controller;

import lombok.RequiredArgsConstructor;
import org.layer.common.annotation.MemberId;
import org.layer.domain.actionItem.api.ActionItemApi;
import org.layer.domain.actionItem.dto.CreateActionItemRequest;
import org.layer.domain.actionItem.dto.CreateActionItemResponse;
import org.layer.domain.actionItem.dto.DeleteActionItemRequest;
import org.layer.domain.actionItem.dto.TeamActionItemRequest;
import org.layer.domain.actionItem.service.ActionItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateActionItemResponse> memberActionItem(@MemberId Long memberId) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateActionItemResponse> teamActionItem(@MemberId Long memberId, TeamActionItemRequest teamActionItemRequest) {
        return null;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateActionItemResponse> deleteActionItem(@MemberId Long memberId, DeleteActionItemRequest deleteActionItemRequest) {
        return null;
    }
}
