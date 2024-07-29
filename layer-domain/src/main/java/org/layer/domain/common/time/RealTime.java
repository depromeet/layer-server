package org.layer.domain.common.time;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class RealTime implements Time {
	@Override
	public LocalDateTime now() {
		return LocalDateTime.now();
	}
}
