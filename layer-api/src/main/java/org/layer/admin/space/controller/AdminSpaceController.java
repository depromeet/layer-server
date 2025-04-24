package org.layer.admin.space.controller;

import lombok.RequiredArgsConstructor;

import org.layer.admin.space.controller.dto.AdminSpaceCountGetResponse;
import org.layer.admin.space.controller.dto.AdminSpacesGetResponse;
import org.layer.admin.retrospect.controller.dto.AdminRetrospectCountGetResponse;
import org.layer.admin.space.service.AdminSpaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/admin/space")
@RequiredArgsConstructor
@RestController
public class AdminSpaceController implements AdminSpaceApi {

    private final AdminSpaceService adminSpaceService;
    @Override
    @GetMapping
    public ResponseEntity<AdminSpacesGetResponse> getSpaceData(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("password") String password) {

        return ResponseEntity.ok(adminSpaceService.getSpaceData(startDate, endDate, password));
    }

    @Override
    @GetMapping("/count/user-only")
    public ResponseEntity<AdminSpaceCountGetResponse> getSpaceCount(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("password") String password) {
        return ResponseEntity.ok(adminSpaceService.getSpaceCount(startDate, endDate, password));
    }

    @Override
    @GetMapping("/{spaceId}/retrospect/count")
    public ResponseEntity<AdminRetrospectCountGetResponse> getRetrospectCountInSpace(@RequestParam("startDate") LocalDateTime startDate,
                                                                                     @RequestParam("endDate") LocalDateTime endDate,
                                                                                     @PathVariable("spaceId") Long spaceId,
                                                                                     @RequestParam("password") String password) {
        return ResponseEntity.ok(adminSpaceService.getRetrospectCountInSpace(startDate, endDate, spaceId, password));
    }

}
