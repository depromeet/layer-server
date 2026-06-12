package org.layer.domain.actionItem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.annotation.MemberId;
import org.layer.domain.actionItem.controller.dto.request.ActionItemCreateBySpaceIdRequest;
import org.layer.domain.actionItem.controller.dto.request.ActionItemCreateRequest;
import org.layer.domain.actionItem.controller.dto.request.ActionItemUpdateRequest;
import org.layer.domain.actionItem.controller.dto.request.PersonalActionItemCreateRequest;
import org.layer.domain.actionItem.controller.dto.response.ActionItemCreateResponse;
import org.layer.domain.actionItem.controller.dto.response.MemberActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.PersonalActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.PersonalSpaceActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.PersonalSpaceRetrospectActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.SpaceActionItemGetResponse;
import org.layer.domain.actionItem.controller.dto.response.SpaceRetrospectActionItemGetResponse;
import org.layer.domain.actionItem.service.ActionItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ActionItemCreateResponse> createActionItem(@MemberId Long memberId,
                                                                     @Validated @RequestBody ActionItemCreateRequest actionItemCreateRequest) {
        ActionItemCreateResponse response = actionItemService.createActionItem(memberId,
                actionItemCreateRequest.retrospectId(),
                actionItemCreateRequest.content());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/member")
    public ResponseEntity<MemberActionItemGetResponse> memberActionItem(@MemberId Long currentMemberId) {
        MemberActionItemGetResponse memberActionItems = actionItemService.getMemberActionItemList(currentMemberId);
        return new ResponseEntity<>(memberActionItems, HttpStatus.OK);
    }

    @Override
    @GetMapping("/space/{spaceId}")
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
    public ResponseEntity<Void> deleteActionItem(@MemberId Long memberId, @PathVariable("actionItemId") Long actionItemId) {
        actionItemService.deleteActionItem(memberId, actionItemId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/retrospect/{retrospectId}/update")
    public ResponseEntity<Void> updateActionItem(@MemberId Long memberId,
                                          @PathVariable("retrospectId") Long retrospectId,
                                          @RequestBody ActionItemUpdateRequest actionItemUpdateRequest) {
        actionItemService.updateActionItems(memberId, retrospectId, actionItemUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/create/space/{spaceId}")
    public ResponseEntity<ActionItemCreateResponse> createActionItemBySpaceId(@MemberId Long memberId,
                                                 @Validated @RequestBody ActionItemCreateBySpaceIdRequest actionItemCreateRequest) {
        ActionItemCreateResponse response = actionItemService.createActionItemBySpaceId(memberId,
                actionItemCreateRequest.spaceId(),
                actionItemCreateRequest.content());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/personal/space/{spaceId}")
    public ResponseEntity<PersonalSpaceRetrospectActionItemGetResponse> getPersonalSpaceActionItems(@MemberId Long memberId,
                                                                                                     @PathVariable Long spaceId) {
        return new ResponseEntity<>(actionItemService.getPersonalSpaceActionItemList(memberId, spaceId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/personal/space/{spaceId}/recent")
    public ResponseEntity<PersonalSpaceActionItemGetResponse> getPersonalSpaceRecentActionItems(@MemberId Long memberId,
                                                                                                 @PathVariable Long spaceId) {
        return new ResponseEntity<>(actionItemService.getPersonalSpaceRecentActionItems(memberId, spaceId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/personal/member")
    public ResponseEntity<MemberActionItemGetResponse> getPersonalMemberActionItems(@MemberId Long memberId) {
        return new ResponseEntity<>(actionItemService.getPersonalMemberActionItemList(memberId), HttpStatus.OK);
    }

    @Override
    @PostMapping("/personal/space/{spaceId}/retrospect/{retrospectId}")
    public ResponseEntity<ActionItemCreateResponse> createPersonalActionItem(@MemberId Long memberId,
                                                                             @PathVariable Long spaceId,
                                                                             @PathVariable Long retrospectId,
                                                                             @Validated @RequestBody PersonalActionItemCreateRequest request) {
        ActionItemCreateResponse response = actionItemService.createPersonalActionItem(memberId, spaceId, retrospectId, request.content());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/personal/space/{spaceId}/retrospect/{retrospectId}")
    public ResponseEntity<PersonalActionItemGetResponse> getPersonalActionItems(@MemberId Long memberId,
                                                                                @PathVariable Long spaceId,
                                                                                @PathVariable Long retrospectId) {
        PersonalActionItemGetResponse response = actionItemService.getPersonalActionItems(memberId, spaceId, retrospectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PatchMapping("/personal/space/{spaceId}/retrospect/{retrospectId}/update")
    public ResponseEntity<Void> updatePersonalActionItems(@MemberId Long memberId,
                                                          @PathVariable Long spaceId,
                                                          @PathVariable Long retrospectId,
                                                          @RequestBody ActionItemUpdateRequest request) {
        actionItemService.updatePersonalActionItems(memberId, spaceId, retrospectId, request);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/personal/{actionItemId}")
    public ResponseEntity<Void> deletePersonalActionItem(@MemberId Long memberId,
                                                         @PathVariable Long actionItemId) {
        actionItemService.deletePersonalActionItem(memberId, actionItemId);
        return ResponseEntity.ok().build();
    }
}
