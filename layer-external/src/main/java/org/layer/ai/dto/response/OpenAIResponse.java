package org.layer.ai.dto.response;

import static org.layer.global.exception.OpenAIExceptionType.*;

import java.io.IOException;
import java.util.List;

import org.layer.ai.exception.OpenAIException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OpenAIResponse {
	private String id;
	private String object;
	private long created;
	private String model;
	private List<Choice> choices;
	private Usage usage;
	private String systemFingerprint;

	@Getter
	@NoArgsConstructor
	public static class Choice {
		private int index;
		private Message message;
		private Object logprobs;
		private String finishReason;
	}

	@Getter
	@NoArgsConstructor
	public static class Message {
		private String role;
		private String content;  // This will be a JSON string
		private Object refusal;
	}

	@Getter
	@NoArgsConstructor
	public static class Content {

		@JsonProperty("good_points")
		private List<ContentDetail> goodPoints;

		@JsonProperty("bad_points")
		private List<ContentDetail> badPoints;

		@JsonProperty("improvement_points")
		private List<ContentDetail> improvementPoints;

		@JsonProperty("high_frequency_words")
		private List<ContentDetail> highFrequencyWords;
	}

	@Getter
	@NoArgsConstructor
	public static class ContentDetail {
		private String point;
		private int count;
	}

	@Getter
	@NoArgsConstructor
	public static class Usage {
		private int promptTokens;
		private int completionTokens;
		private int totalTokens;
	}

	// Method to convert content JSON string to Content DTO
	public Content parseContent() {
		ObjectMapper objectMapper = new ObjectMapper();
		Content content = null;
		try {
			content = objectMapper.readValue(this.choices.get(0).getMessage().getContent(), Content.class);
		} catch (IOException e) {
			throw new OpenAIException(INVALID_PARSE);
		}

		return content;
	}
}
