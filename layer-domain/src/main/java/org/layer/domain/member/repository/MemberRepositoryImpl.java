package org.layer.domain.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.layer.domain.member.entity.MemberFeedback;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.layer.domain.answer.entity.QAnswer.answer;
import static org.layer.domain.member.entity.QMember.member;
import static org.layer.domain.space.entity.QMemberSpaceRelation.memberSpaceRelation;
import static org.layer.domain.space.entity.QSpace.space;

@Repository
public class MemberRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<MemberFeedback> findAllMemberFeedback(Long memberId) {

        return Optional.ofNullable(queryFactory
                .select(
                        Projections.constructor(MemberFeedback.class,
                                member.id.as("memberId"),
                                member.name.as("memberName"),
                                member.email.as("email"),
                                member.createdAt.as("memberCreatedAt"),
                                answer.retrospectId.countDistinct().as("retrospectCount"),
                                space.id.countDistinct().as("spaceCount")
                        )
                )
                .from(member)
                .join(memberSpaceRelation).on(member.id.eq(memberSpaceRelation.memberId))
                .leftJoin(space).on(space.id.eq(memberSpaceRelation.space.id))
                .leftJoin(answer).on(answer.memberId.eq(member.id))
                .where(member.id.eq(memberId))
                .groupBy(member.id)
                .fetchOne());
    }
}
