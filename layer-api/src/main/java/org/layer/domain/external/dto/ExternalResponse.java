package org.layer.domain.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.layer.external.ncp.exception.ExternalExeption;

import java.util.Optional;

import static org.layer.common.exception.ExternalExceptionType.INTERNAL_SERVER_ERROR;

public class ExternalResponse {

    @Builder
    @Schema(description = "Presigned URL 응답")
    public record GetPreSignedURLResponse(
            @Schema(description = "생성된 Presigned URL")
            @NotNull
            String url
    ) {
        public static GetPreSignedURLResponse toResponse(String url) {
            return Optional.ofNullable(url).map(it -> GetPreSignedURLResponse.builder().url(url).build()).orElseThrow(() -> new ExternalExeption(INTERNAL_SERVER_ERROR));
        }
    }
}
