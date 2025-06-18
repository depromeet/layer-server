package org.layer.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Meta {

    @NotNull
    private boolean hasNextPage;

    private Long cursor;
}
