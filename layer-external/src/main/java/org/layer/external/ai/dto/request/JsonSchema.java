package org.layer.external.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JsonSchema {

	@JsonProperty("name")
	private final String name;

	@JsonProperty("strict")
	private final boolean strict;

	@JsonProperty("schema")
	private final Schema schema;
}

