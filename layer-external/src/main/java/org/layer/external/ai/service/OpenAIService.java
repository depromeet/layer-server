package org.layer.external.ai.service;

import java.util.List;

import org.layer.external.ai.constant.OpenAIFactory;
import org.layer.external.ai.constant.OpenAIProperties;
import org.layer.external.ai.dto.request.MessageRequest;
import org.layer.external.ai.dto.request.OpenAIRequest;
import org.layer.external.ai.dto.request.ResponseFormat;
import org.layer.external.ai.dto.response.OpenAIResponse;
import org.layer.external.ai.infra.OpenAIClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenAIService {

	private final OpenAIClient openAIClient;
	private final OpenAIProperties openAIProperties;

	public OpenAIResponse createAnalyze(String answerContent) {
		MessageRequest systemMessage = new MessageRequest("system", openAIProperties.getSystemContent());
		MessageRequest userMessage = new MessageRequest("user", answerContent);
		List<MessageRequest> messages = List.of(systemMessage, userMessage);

		ResponseFormat responseFormat = OpenAIFactory.createJsonSchema();

		return openAIClient.createAnalyze(
			openAIProperties.getApiKey(),
			new OpenAIRequest(openAIProperties.getModel(), messages, openAIProperties.getMaxTokens(), responseFormat)
		);
	}
}
