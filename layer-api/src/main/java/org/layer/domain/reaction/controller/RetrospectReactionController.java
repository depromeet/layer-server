package org.layer.domain.reaction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.layer.annotation.MemberId;
import org.layer.domain.reaction.controller.dto.request.RetrospectReactionCreateRequest;
import org.layer.domain.reaction.controller.dto.response.RetrospectReactionListGetResponse;
import org.layer.domain.reaction.service.RetrospectReactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect/{retrospectId}/reaction")
public class RetrospectReactionController implements RetrospectReactionApi {

    private final RetrospectReactionService retrospectReactionService;

    @Override
    @PostMapping
    public ResponseEntity<Void> createReaction(
            @PathVariable("spaceId") Long spaceId,
            @PathVariable("retrospectId") Long retrospectId,
            @RequestBody @Valid RetrospectReactionCreateRequest request,
            @MemberId Long memberId
    ) {
        retrospectReactionService.createReaction(spaceId, retrospectId, request, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @DeleteMapping("/{retrospectReactionId}")
    public ResponseEntity<Void> deleteReaction(
            @PathVariable("spaceId") Long spaceId,
            @PathVariable("retrospectId") Long retrospectId,
            @PathVariable("retrospectReactionId") Long retrospectReactionId,
            @MemberId Long memberId
    ) {
        retrospectReactionService.deleteReaction(spaceId, retrospectId, retrospectReactionId, memberId);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<RetrospectReactionListGetResponse> getRetrospectReactions(
            @PathVariable("spaceId") Long spaceId,
            @PathVariable("retrospectId") Long retrospectId,
            @MemberId Long memberId
    ) {
        RetrospectReactionListGetResponse response = retrospectReactionService.getRetrospectReactions(spaceId, retrospectId, memberId);
        return ResponseEntity.ok(response);
    }
}
