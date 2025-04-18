package org.layer.ncp.dto;

import lombok.Builder;

public class NcpResponse {

    @Builder
    public record PresignedResult(
            String presignedUrl,
            String imageUrl
    ) {

        public static PresignedResult toResponse(String presignedUrl,
                                                 String imageUrl) {
            return PresignedResult.builder().presignedUrl(presignedUrl).imageUrl(imageUrl).build();

        }
    }
}
