package org.layer.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.QMember;
import org.layer.domain.member.entity.SocialType;

import java.util.Optional;


public class MemberRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Member> findValidMember(String socialId, SocialType socialType) {
        Member member = queryFactory
                .select(QMember.member)
                .from(QMember.member)
                .where(QMember.member.socialId.eq(socialId)
                        .and(QMember.member.socialType.eq(socialType))
                        .and(QMember.member.deletedAt.isNull())
                ).fetchFirst();

        return Optional.ofNullable(member);
    }
}
