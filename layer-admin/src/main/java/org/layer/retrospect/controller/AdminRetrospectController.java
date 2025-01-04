package org.layer.retrospect.controller;

import lombok.RequiredArgsConstructor;
import org.layer.domain.retrospect.dto.AdminRetrospectCountGroupBySpaceGetResponse;
import org.layer.retrospect.controller.dto.AdminRetrospectCountGetResponse;
import org.layer.retrospect.controller.dto.AdminRetrospectsGetResponse;
import org.layer.retrospect.service.AdminRetrospectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/retrospect")
@RequiredArgsConstructor
@RestController
public class AdminRetrospectController implements AdminRetrospectApi {
    private final AdminRetrospectService adminRetrospectService;

    @Override
    @GetMapping
    public ResponseEntity<AdminRetrospectsGetResponse> getRetrospectData(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("password") String password) {

        return ResponseEntity.ok(adminRetrospectService.getRetrospectData(startDate, endDate, password));
    }

    @Override
    @GetMapping("/count/user-only")
    public ResponseEntity<AdminRetrospectCountGetResponse> getRetrospectCount(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("password") String password) {

        return ResponseEntity.ok(adminRetrospectService.getRetrospectCount(startDate, endDate, password));
    }

    @Override
    @GetMapping("/count/group-by-space")
    public ResponseEntity<List<AdminRetrospectCountGroupBySpaceGetResponse>> getRetrospectCountGroupBySpace(LocalDateTime startDate, LocalDateTime endDate, String password) {
        return ResponseEntity.ok(adminRetrospectService.getRetrospectCountGroupSpace(startDate, endDate, password));
    }

}