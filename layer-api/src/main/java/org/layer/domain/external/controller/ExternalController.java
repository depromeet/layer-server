package org.layer.domain.external.controller;

import lombok.RequiredArgsConstructor;
import org.layer.domain.external.api.ExternalApi;
import org.layer.domain.external.dto.ExternalRequest;
import org.layer.domain.external.dto.ExternalResponse;
import org.layer.external.ncp.service.NcpService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
public class ExternalController implements ExternalApi {

    private final NcpService ncpService;

    @Override
    @GetMapping("/image/presigned")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExternalResponse.GetPreSignedURLResponse> getPresignedURL(Long memberId, ExternalRequest.GetPreSignedURLRequest getPreSignedURLRequest) {
        String url = ncpService.getPreSignedUrl(memberId, getPreSignedURLRequest.domain());

        return ResponseEntity.ok(ExternalResponse.GetPreSignedURLResponse.toResponse(url));
    }
}
