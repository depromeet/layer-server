package org.layer.external.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Properties {

	@JsonProperty("good_points")
	private final ArrayProperty goodPoints;

	@JsonProperty("bad_points")
	private final ArrayProperty badPoints;

	@JsonProperty("improvement_points")
	private final ArrayProperty improvementPoints;
}

