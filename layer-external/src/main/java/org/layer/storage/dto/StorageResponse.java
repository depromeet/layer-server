package org.layer.storage.dto;

import lombok.Builder;

public class StorageResponse {

    @Builder
    public record PresignedResult(
            String presignedUrl,
            String imageUrl
    ) {

        public static PresignedResult toResponse(String presignedUrl, String imageUrl) {
            return PresignedResult.builder().presignedUrl(presignedUrl).imageUrl(imageUrl).build();
        }
    }
}
