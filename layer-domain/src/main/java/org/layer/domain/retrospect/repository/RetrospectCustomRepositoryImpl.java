package org.layer.domain.retrospect.repository;

import static org.layer.domain.retrospect.entity.QRetrospect.*;

import java.time.LocalDateTime;

import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class RetrospectCustomRepositoryImpl implements RetrospectCustomRepository {
	private final JPAQueryFactory queryFactory;
	public RetrospectCustomRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	@Transactional
	public void updateRetrospectStatus(LocalDateTime now) {
		queryFactory
			.update(retrospect)
			.where(Expressions.asDate(now).after(retrospect.deadline))
			.set(retrospect.retrospectStatus, RetrospectStatus.DONE)
			.execute();

			// LocalDate.now(ZoneId.of("Asia/Seoul"))
	}
}
