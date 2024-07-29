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
                        
            - GetPreSignedURLResponse.presignedUrl: 이미지 저장을 위한 요청 URL<br />
            - GetPreSignedURLResponse.imageUrl:     저장 완료된 이미지 요청 URL
                        
            # 참고사항
                        
            presigned URL 요청 시 헤더에 담긴 Content-type에 따라 파일의 확장자가 정해집니다.
            사용처에 따라 정확한 Content-type으로 요청 부탁드립니다.
                        
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
