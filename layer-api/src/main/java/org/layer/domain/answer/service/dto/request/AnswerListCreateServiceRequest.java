package org.layer.domain.answer.service.dto.request;

import java.util.List;

public record AnswerListCreateServiceRequest(
	List<AnswerCreateServiceRequest> requests
) {
	public static AnswerListCreateServiceRequest of(List<AnswerCreateServiceRequest> requests){
		return new AnswerListCreateServiceRequest(requests);
	}
}
