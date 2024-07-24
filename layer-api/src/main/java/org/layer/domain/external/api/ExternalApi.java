package org.layer.domain.external.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.layer.common.annotation.MemberId;
import org.layer.domain.external.dto.ExternalRequest;
import org.layer.domain.external.dto.ExternalResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "외부 API")
public interface ExternalApi {
    @Operation(summary = "Presigned URL 발급받기",
            method = "POST", description = """
            이미지 업로드를 위한 Presigned URL 발급합니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExternalResponse.GetPreSignedURLResponse.class)
                            )
                    }
            )
    })
    ResponseEntity<ExternalResponse.GetPreSignedURLResponse> getPresignedURL(@MemberId Long memberId, ExternalRequest.GetPreSignedURLRequest getPreSignedURLRequest);

}
