package org.layer.domain.retrospect.dto;

import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.space.entity.Space;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpaceRetrospectDto {

	private final Space space;
	private final Retrospect retrospect;
}
