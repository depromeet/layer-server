package org.layer.domain.retrospect.service;

import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetrospectService {

	private final RetrospectRepository retrospectRepository;

	public void create(Long spaceId, Long formId, String title, String introduction){
		Retrospect retrospect = Retrospect.builder()
			.title(title)
			.formId(formId)
			.spaceId(spaceId)
			.introduction(introduction)
			.retrospectStatus(RetrospectStatus.PROCEEDING)
			.build();

		retrospectRepository.save(retrospect);
	}
}
