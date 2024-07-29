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
            @Schema(title = "생성된 Presigned URL", description = """
                    이미지 저장을 위한 요청 URL<br />
                    *요청시 보낸 Content-type에 따라 확장자가 결정됩니다*
                    """)
            @NotNull
            String presignedUrl,

            @Schema(title = "업르드된 이미지 주소", description = """
                    도메인 별로 엔티티 생성 시 해당 파라미터 객체 유무 확인 로직이 있습니다.
                    업로드 성공 이후 엔티티 생성 요청 부탁드립니다
                    """)
            String imageUrl
    ) {
        public static GetPreSignedURLResponse toResponse(String presignedUrl, String imageUrl) {
            return Optional.ofNullable(presignedUrl)
                    .map(it -> GetPreSignedURLResponse.builder()
                            .presignedUrl(presignedUrl)
                            .imageUrl(imageUrl)
                            .build()
                    ).orElseThrow(() -> new ExternalExeption(INTERNAL_SERVER_ERROR));
        }
    }
}
