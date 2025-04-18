package org.layer.ai.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("openai")
public class OpenAIProperties {
	private final String systemContent;
	private final String apiKey;
	private final String model;
	private final int maxTokens;
}
