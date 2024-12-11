package org.layer.common.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class RecentActivityDto {
	private final LocalDateTime recentActivityDate;

	@JsonCreator
	public RecentActivityDto(LocalDateTime recentActivityDate) {
		this.recentActivityDate = recentActivityDate;
	}
}
