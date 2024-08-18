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
                .and(cursorId == null ? null : space.id.gt(cursorId))
                .and(hasCategory(category));

        return getSpaceWithMemberCountQuery()
                .where(predicate)
                .groupBy(space.id)
                .orderBy(space.createdAt.desc())
                .limit(pageSize + 1)
                .fetch();
    }

    @Override
    public Optional<SpaceWithMemberCount> findByIdAndJoinedMemberId(Long spaceId, Long memberId) {

        var foundSpace = getSpaceWithMemberCountQuery().where(memberSpaceRelation.memberId.eq(memberId).and(space.id.eq(spaceId))).fetchOne();


        if (foundSpace == null || isSpaceWithMemberCountEmpty(foundSpace)) {
            return Optional.empty();
        }
        // TODO: 커스텀 에러로 변경
        return Optional.of(foundSpace);
    }

    @Override
    public Long updateSpace(Long spaceId, SpaceCategory category, List<SpaceField> fieldList, String name, String introduction, String bannerUrl) {
        var query = queryFactory.update(space);

        // null 값 제거
        Optional.ofNullable(category).ifPresent(it -> query.set(space.category, it));
        Optional.ofNullable(fieldList).ifPresent(it -> query.set(new ArrayList<>(), it));
        Optional.ofNullable(name).ifPresent(it -> query.set(space.name, it));
        Optional.ofNullable(introduction).ifPresent(it -> query.set(space.introduction, it));
        Optional.ofNullable(bannerUrl).ifPresent(it -> query.set(space.bannerUrl, it));

        return query.where(space.id.eq(spaceId)).execute();
    }

    @Override
    public List<SpaceMember> findAllSpaceMemberBySpaceIdWithIsLeader(Long spaceId) {
        return queryFactory
                .select(new QSpaceMember(
                                member.id,
                                member.profileImageUrl,
                                member.name,
                                space.leaderId.eq(member.id)
                        )
                )
                .from(memberSpaceRelation)
                .leftJoin(member)
                .on(memberSpaceRelation.memberId.eq(member.id))
                .leftJoin(space)
                .on(memberSpaceRelation.space.id.eq(space.id))
                .where(memberSpaceRelation.space.id.eq(spaceId))
                .orderBy(memberSpaceRelation.createdAt.asc())
                .fetch();
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
                                memberCountRelationTable.space.id.count().as("memberCount"),
                                space.bannerUrl
                        ))
                .from(space)
                .leftJoin(memberSpaceRelation).on(space.id.eq(memberSpaceRelation.space.id))
                .leftJoin(memberCountRelationTable).on(space.id.eq(memberCountRelationTable.space.id))
                .leftJoin(member).on(space.leaderId.eq(member.id))
                .leftJoin(form).on(space.formId.eq(form.spaceId));

    }


    private BooleanExpression hasCategory(Optional<SpaceCategory> category) {
        return category.map(space.category::eq).orElse(null);
    }

    private boolean isSpaceWithMemberCountEmpty(SpaceWithMemberCount space) {
        return space.getId() == null && space.getName() == null && space.getMemberCount() == 0;
    }
}
