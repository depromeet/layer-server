package org.layer.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.layer.domain.member.entity.Member;
import org.layer.domain.member.entity.QMember;
import org.layer.domain.member.entity.SocialType;
import org.layer.domain.member.exception.MemberException;

import java.util.List;
import java.util.Optional;

import static org.layer.global.exception.MemberExceptionType.NOT_FOUND_USER;


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

    @Override
    public Member findValidMemberByIdOrThrow(Long memberId) {
        List<Member> fetch = queryFactory
                .select(QMember.member)
                .from(QMember.member)
                .where(QMember.member.id.eq(memberId)
                        .and(QMember.member.deletedAt.isNull())
                ).fetch();

        if(fetch.isEmpty()) {
            throw new MemberException(NOT_FOUND_USER);
        }

        return fetch.get(0);
    }
}