package org.layer.domain.reaction.controller;

import lombok.RequiredArgsConstructor;
import org.layer.annotation.MemberId;
import org.layer.domain.reaction.controller.dto.response.ReactionListGetResponse;
import org.layer.domain.reaction.service.RetrospectReactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reaction")
public class ReactionController implements ReactionApi {

    private final RetrospectReactionService retrospectReactionService;

    @Override
    @GetMapping
    public ResponseEntity<ReactionListGetResponse> getAllReactions() {
        return ResponseEntity.ok(retrospectReactionService.getAllReactions());
    }

    @Override
    @GetMapping("/recent")
    public ResponseEntity<ReactionListGetResponse> getRecentReactions(
            @MemberId Long memberId,
            @RequestParam(name = "limit", defaultValue = "8") int limit
    ) {
        return ResponseEntity.ok(retrospectReactionService.getRecentReactions(memberId, limit));
    }
}
