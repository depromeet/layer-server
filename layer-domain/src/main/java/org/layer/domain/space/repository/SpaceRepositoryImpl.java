package org.layer.domain.space.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import org.layer.common.dto.CursorPageReq;
import org.layer.common.dto.CursorPageRes;
import org.layer.domain.space.dto.QSpaceWithMemberCount;
import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.QMemberSpaceRelation;
import org.layer.domain.space.entity.SpaceCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.layer.domain.form.entity.QForm.form;
import static org.layer.domain.member.entity.QMember.member;
import static org.layer.domain.space.entity.QMemberSpaceRelation.memberSpaceRelation;
import static org.layer.domain.space.entity.QSpace.space;

@Repository
@Slf4j
public class SpaceRepositoryImpl implements SpaceCustomRepository {

	private final JPAQueryFactory queryFactory;

	public SpaceRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public CursorPageRes<SpaceWithMemberCount> findAllSpacesByMemberIdAndCategoryAndCursor(
		Long memberId, SpaceCategory category, CursorPageReq cursorPageReq
	) {
		QMemberSpaceRelation msrUser = memberSpaceRelation;
		QMemberSpaceRelation msrAll = new QMemberSpaceRelation("msr_all");

		BooleanExpression isMemberInSpace = msrUser.memberId.eq(memberId);
		BooleanExpression hasCursor = cursorPageReq.isFirstPage() ? null : space.id.lt(cursorPageReq.getCursorId());
		BooleanExpression hasCategory = category == null ? null : space.category.eq(category);
		BooleanExpression isLeaderNotDeleted = member.deletedAt.isNull();

		List<SpaceWithMemberCount> data = queryFactory
			.select(new QSpaceWithMemberCount(
				space.id,
				space.createdAt,
				space.updatedAt,
				space.category,
				space.name,
				space.introduction,
				member,
				space.formId,
				form.formTag,
				msrAll.space.id.count().as("memberCount"),
				space.bannerUrl
			))
			.from(msrUser)
			.leftJoin(space).on(space.id.eq(msrUser.space.id))
			.leftJoin(member).on(space.leaderId.eq(member.id))
			.leftJoin(msrAll).on(msrAll.space.id.eq(space.id))
			.leftJoin(form).on(space.formId.eq(form.id))
			.where(
				isMemberInSpace,
				hasCursor,
				hasCategory,
				isLeaderNotDeleted
			)
			.groupBy(space.id)
			.orderBy(space.createdAt.desc())
			.limit(cursorPageReq.getFetchSize())
			.fetch();

		return new CursorPageRes<>(data, cursorPageReq.getPageSize(), SpaceWithMemberCount::getId);
	}
}
