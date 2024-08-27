package org.layer.domain.retrospect.repository;

import java.time.LocalDateTime;

public interface RetrospectCustomRepository {

	void updateRetrospectStatus(LocalDateTime now);
}
