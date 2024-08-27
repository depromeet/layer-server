package org.layer.external.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArrayProperty {

	@JsonProperty("type")
	private final String type;

	@JsonProperty("description")
	private final String description;

	@JsonProperty("items")
	private final Items items;
}

