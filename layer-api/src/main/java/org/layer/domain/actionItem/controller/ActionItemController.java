package org.layer.domain.actionItem.controller;

import org.layer.common.annotation.MemberId;
import org.layer.domain.actionItem.api.ActionItemApi;
import org.layer.domain.actionItem.dto.CreateActionItemRequest;
import org.layer.domain.actionItem.dto.CreateActionItemResponse;
import org.layer.domain.actionItem.dto.DeleteActionItemRequest;
import org.layer.domain.actionItem.dto.TeamActionItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActionItemController implements ActionItemApi {
    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateActionItemResponse> createActionItem(@MemberId Long memberId, CreateActionItemRequest createActionItemRequest) {
        return null;
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
