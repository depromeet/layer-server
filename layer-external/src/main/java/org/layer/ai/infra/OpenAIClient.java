package org.layer.ai.infra;

import org.layer.ai.dto.response.OpenAIResponse;
import org.layer.ai.dto.request.OpenAIRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openAiClient", url = "https://api.openai.com/v1")
@Component
public interface OpenAIClient {

	@PostMapping("/chat/completions")
	OpenAIResponse createAnalyze(
		@RequestHeader("Authorization") String authorization,
		@RequestBody OpenAIRequest request);

}
