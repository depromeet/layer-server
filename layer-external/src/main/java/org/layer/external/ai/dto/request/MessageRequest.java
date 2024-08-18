package org.layer.external.ai.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageRequest {
	private final String role;
	private final String content;
}
