package org.layer.domain.answer.controller.dto.request;

import java.util.List;

public record AnswerListCreateRequest(
	List<AnswerCreateRequest> requests
) {
}
