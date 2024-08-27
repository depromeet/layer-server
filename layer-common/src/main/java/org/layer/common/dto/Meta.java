package org.layer.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Meta {
    @Schema()
    @NotNull
    private boolean hasNextPage;

    @Schema(nullable = true)
    private Long cursor;
}
