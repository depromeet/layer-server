package org.layer.domain.external.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.layer.external.ncp.enums.ImageDomain;

public class ExternalRequest {

    @Schema(description = "Presigned URL 발급받기")
    public record GetPreSignedURLRequest(
            @Schema(description = "발급받을 이미지의 활용 도메인")
            ImageDomain domain
    ) {
    }
}
