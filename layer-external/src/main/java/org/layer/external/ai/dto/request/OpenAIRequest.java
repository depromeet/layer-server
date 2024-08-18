package org.layer.external.ai.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OpenAIRequest {

	@JsonProperty("model")
	private final String model;

	@JsonProperty("messages")
	private final List<MessageRequest> messages;

	@JsonProperty("max_tokens")
	private final int maxTokens;

	@JsonProperty("response_format")
	private final ResponseFormat responseFormat;
}
