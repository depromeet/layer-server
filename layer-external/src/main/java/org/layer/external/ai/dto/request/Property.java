package org.layer.external.ai.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Property {
	private final String type;
	private final String description;
}

