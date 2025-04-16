package org.layer.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.layer.annotation.MemberId;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.layer.domain.answer.controller.dto.request.AnswerListUpdateRequest;
import org.layer.domain.answer.controller.dto.response.AnswerListGetResponse;
import org.layer.domain.answer.controller.dto.response.TemporaryAnswerListResponse;
import org.layer.domain.answer.controller.dto.response.WrittenAnswerGetResponse;
import org.layer.domain.answer.controller.dto.response.WrittenAnswerListResponse;
import org.layer.domain.answer.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/retrospect/{retrospectId}/answer")
public class AnswerController implements AnswerApi {
    private final AnswerService answerService;

    @Override
    @PostMapping
    public ResponseEntity<Void> createAnswer(@PathVariable("spaceId") Long spaceId,
                                             @PathVariable("retrospectId") Long retrospectId,
                                             @RequestBody @Valid AnswerListCreateRequest request, @MemberId Long memberId) {

        answerService.create(request, spaceId, retrospectId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/temp")
    public ResponseEntity<TemporaryAnswerListResponse> getTemporaryAnswer(@PathVariable("spaceId") Long spaceId,
                                                                          @PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId) {

        TemporaryAnswerListResponse dto = answerService.getTemporaryAnswer(spaceId, retrospectId, memberId);

        return ResponseEntity.ok().body(dto);
    }

    @Override
    @GetMapping("/analyze")
    public ResponseEntity<AnswerListGetResponse> getAnalyzeAnswer(@PathVariable("spaceId") Long spaceId,
                                                                  @PathVariable("retrospectId") Long retrospectId, @MemberId Long memberId) {
        AnswerListGetResponse response = answerService.getAnalyzeAnswers(spaceId, retrospectId, memberId);

        return ResponseEntity.ok().body(response);
    }

    @Override
    @GetMapping("/written")
    public ResponseEntity<WrittenAnswerListResponse> getWrittenAnswersBeforeRetrospectIsDone(
            @PathVariable("spaceId") Long spaceId,
            @PathVariable("retrospectId") Long retrospectId,
            @MemberId Long memberId
    ) {
        List<WrittenAnswerGetResponse> response = answerService.getWrittenAnswer(spaceId, retrospectId, memberId);
        return ResponseEntity.ok().body(WrittenAnswerListResponse.of(response));
    }

    @Override
    @PutMapping
    public ResponseEntity<Void> updateWrittenAnswers(Long spaceId, Long retrospectId, Long memberId, AnswerListUpdateRequest answerListUpdateRequest) {
        answerService.update(answerListUpdateRequest, spaceId, retrospectId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
