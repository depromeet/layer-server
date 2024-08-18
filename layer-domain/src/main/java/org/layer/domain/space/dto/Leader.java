package org.layer.domain.space.dto;


import lombok.Builder;

@Builder
public record Leader(
        Long id,
        String name
) {
}
