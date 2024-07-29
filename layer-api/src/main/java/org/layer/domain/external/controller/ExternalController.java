package org.layer.domain.external.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.MemberId;
import org.layer.domain.external.api.ExternalApi;
import org.layer.domain.external.dto.ExternalRequest;
import org.layer.domain.external.dto.ExternalResponse;
import org.layer.external.ncp.dto.NcpResponse;
import org.layer.external.ncp.service.NcpService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/external")
public class ExternalController implements ExternalApi {

    private final NcpService ncpService;

    @Override
    @GetMapping("/image/presigned")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExternalResponse.GetPreSignedURLResponse> getPresignedURL(@MemberId Long memberId, ExternalRequest.GetPreSignedURLRequest getPreSignedURLRequest) {
        NcpResponse.PresignedResult presignedResult = ncpService.getPreSignedUrl(memberId, getPreSignedURLRequest.domain());


        return ResponseEntity.ok(ExternalResponse.GetPreSignedURLResponse.toResponse(presignedResult.presignedUrl(), presignedResult.imageUrl()));
    }
}
