package org.layer.external.ai.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Schema {

	@JsonProperty("type")
	private final String type;

	@JsonProperty("properties")
	private final Properties properties;

	@JsonProperty("required")
	private final List<String> required;

	@JsonProperty("additionalProperties")
	private final boolean additionalProperties;
}

