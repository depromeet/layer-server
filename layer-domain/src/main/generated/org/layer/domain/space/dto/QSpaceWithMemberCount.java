package org.layer.domain.space.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * org.layer.domain.space.dto.QSpaceWithMemberCount is a Querydsl Projection type for SpaceWithMemberCount
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSpaceWithMemberCount extends ConstructorExpression<SpaceWithMemberCount> {

    private static final long serialVersionUID = -864849853L;

    public QSpaceWithMemberCount(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt, com.querydsl.core.types.Expression<org.layer.domain.space.entity.SpaceCategory> category, com.querydsl.core.types.Expression<org.layer.domain.space.entity.SpaceField> field, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> introduction, com.querydsl.core.types.Expression<Long> leaderId, com.querydsl.core.types.Expression<Long> formId, com.querydsl.core.types.Expression<Long> memberCount) {
        super(SpaceWithMemberCount.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, org.layer.domain.space.entity.SpaceCategory.class, org.layer.domain.space.entity.SpaceField.class, String.class, String.class, long.class, long.class, long.class}, id, createdAt, updatedAt, category, field, name, introduction, leaderId, formId, memberCount);
    }

}

