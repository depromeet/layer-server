package org.layer.common.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Meta {

    private boolean hasNextPage;

    private Long cursor;
}
