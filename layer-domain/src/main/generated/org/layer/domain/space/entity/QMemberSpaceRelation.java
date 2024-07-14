package org.layer.domain.space.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberSpaceRelation is a Querydsl query type for MemberSpaceRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberSpaceRelation extends EntityPathBase<MemberSpaceRelation> {

    private static final long serialVersionUID = -1641406808L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberSpaceRelation memberSpaceRelation = new QMemberSpaceRelation("memberSpaceRelation");

    public final org.layer.domain.QBaseEntity _super = new org.layer.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final QSpace space;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberSpaceRelation(String variable) {
        this(MemberSpaceRelation.class, forVariable(variable), INITS);
    }

    public QMemberSpaceRelation(Path<? extends MemberSpaceRelation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberSpaceRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberSpaceRelation(PathMetadata metadata, PathInits inits) {
        this(MemberSpaceRelation.class, metadata, inits);
    }

    public QMemberSpaceRelation(Class<? extends MemberSpaceRelation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.space = inits.isInitialized("space") ? new QSpace(forProperty("space")) : null;
    }

}

