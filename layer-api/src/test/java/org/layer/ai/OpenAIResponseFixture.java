package org.layer.ai;

import java.util.List;

import org.layer.ai.dto.response.OpenAIResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAIResponseFixture {
	public static OpenAIResponse create() {
		// ContentDetail 더미 데이터 생성
		List<OpenAIResponse.ContentDetail> goodPoints = List.of(
			createDetail("팀워크가 좋았어요", 3)
		);
		List<OpenAIResponse.ContentDetail> badPoints = List.of(
			createDetail("시간 관리가 부족했어요", 2)
		);
		List<OpenAIResponse.ContentDetail> improvementPoints = List.of(
			createDetail("기획 단계 명확화 필요", 1)
		);
		List<OpenAIResponse.ContentDetail> highFrequencyWords = List.of(
			createDetail("소통", 5)
		);

		// Content 객체 → JSON으로 직렬화
		OpenAIResponse.Content content = new OpenAIResponse.Content();
		content.setGoodPoints(goodPoints);
		content.setBadPoints(badPoints);
		content.setImprovementPoints(improvementPoints);
		content.setHighFrequencyWords(highFrequencyWords);

		String jsonContent;
		try {
			jsonContent = new ObjectMapper().writeValueAsString(content);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to mock OpenAIResponse", e);
		}

		// Message → Choice → OpenAIResponse 생성
		OpenAIResponse.Message message = new OpenAIResponse.Message();
		message.setContent(jsonContent);
		message.setRole("assistant");

		OpenAIResponse.Choice choice = new OpenAIResponse.Choice();
		choice.setMessage(message);
		choice.setIndex(0);

		OpenAIResponse response = new OpenAIResponse();
		response.setChoices(List.of(choice));
		response.setId("mock-id");
		response.setModel("gpt-4");
		response.setCreated(System.currentTimeMillis());

		OpenAIResponse.Usage usage = new OpenAIResponse.Usage();
		usage.setPromptTokens(10);
		usage.setCompletionTokens(20);
		usage.setTotalTokens(30);
		response.setUsage(usage);

		return response;
	}

	private static OpenAIResponse.ContentDetail createDetail(String point, int count) {
		OpenAIResponse.ContentDetail detail = new OpenAIResponse.ContentDetail();
		detail.setPoint(point);
		detail.setCount(count);
		return detail;
	}
}
