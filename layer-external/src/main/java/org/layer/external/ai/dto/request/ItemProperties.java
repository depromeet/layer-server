package org.layer.external.ai.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemProperties {
	private final Property point;
	private final Property count;
}

