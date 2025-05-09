package org.layer.domain.space.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.space.dto.QSpaceMember;
import org.layer.domain.space.dto.QSpaceWithMemberCount;
import org.layer.domain.space.dto.SpaceMember;
import org.layer.domain.space.dto.SpaceWithMemberCount;
import org.layer.domain.space.entity.QMemberSpaceRelation;
import org.layer.domain.space.entity.SpaceCategory;
import org.layer.domain.space.entity.SpaceField;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<SpaceWithMemberCount> findAllSpacesByMemberIdAndCategoryAndCursor(Long memberId, Long cursorId, Optional<SpaceCategory> category, int pageSize) {
        BooleanExpression predicate = memberSpaceRelation.memberId.eq(memberId)
                .and(cursorId == 0 ? null : space.id.lt(cursorId))
                .and(hasCategory(category));

        return getSpaceWithMemberCountQuery()
                .where(predicate)
                .groupBy(space.id)
                .limit(pageSize + 1)
                .fetch();
    }

    private BooleanExpression hasCategory(Optional<SpaceCategory> category) {
        return category.map(space.category::eq).orElse(null);
    }

    private JPAQuery<SpaceWithMemberCount> getSpaceWithMemberCountQuery() {
        QMemberSpaceRelation memberCountRelationTable = new QMemberSpaceRelation("msr");
        return queryFactory.select(
                        new QSpaceWithMemberCount(
                                space.id,
                                space.createdAt,
                                space.updatedAt,
                                space.category,
                                space.fieldList,
                                space.name,
                                space.introduction,
                                member,
                                space.formId,
                                form.formTag,
                                memberCountRelationTable.space.id.count().as("memberCount"),
                                space.bannerUrl
                        ))
                .from(space)
                .leftJoin(memberSpaceRelation).on(space.id.eq(memberSpaceRelation.space.id))
                .leftJoin(memberCountRelationTable).on(space.id.eq(memberCountRelationTable.space.id))
                .leftJoin(member).on(space.leaderId.eq(member.id))
                .leftJoin(form).on(space.formId.eq(form.id))
                .leftJoin(member).on(memberSpaceRelation.memberId.eq(member.id)) // memberId와 member의 id 조인
                .where(member.deletedAt.isNull()) // 삭제되지 않은 회원만
                .orderBy(space.createdAt.desc())
                .orderBy(form.id.desc())
                .limit(1);

    }
}
