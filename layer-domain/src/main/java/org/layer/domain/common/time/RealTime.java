package org.layer.domain.common.time;

import java.time.LocalDateTime;

public class RealTime implements Time {
	@Override
	public LocalDateTime now() {
		return LocalDateTime.now();
	}
}
