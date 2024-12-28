package org.layer.space.controller;

import lombok.RequiredArgsConstructor;
import org.layer.space.controller.dto.AdminSpaceCountGetResponse;
import org.layer.space.controller.dto.AdminSpacesGetResponse;
import org.layer.space.service.AdminSpaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/space")
@RequiredArgsConstructor
@RestController
public class AdminSpaceController implements AdminSpaceApi {

    private final AdminSpaceService adminSpaceService;
    @Override
    @GetMapping("/space")
    public ResponseEntity<AdminSpacesGetResponse> getSpaceData(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("password") String password) {

        return ResponseEntity.ok(adminSpaceService.getSpaceData(startDate, endDate, password));
    }

    @Override
    @GetMapping("/space/count/user-only")
    public ResponseEntity<AdminSpaceCountGetResponse> getSpaceCount(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("password") String password) {
        return ResponseEntity.ok(adminSpaceService.getSpaceCount(startDate, endDate, password));
    }
}
