package org.layer.external.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseFormat {

	@JsonProperty("type")
	private final String type;

	@JsonProperty("json_schema")
	private final JsonSchema jsonSchema;
}
