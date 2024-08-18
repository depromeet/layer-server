package org.layer.external.ai.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Items {

	private final String type;
	private final ItemProperties properties;
	private final List<String> required;
	private final boolean additionalProperties;

}

