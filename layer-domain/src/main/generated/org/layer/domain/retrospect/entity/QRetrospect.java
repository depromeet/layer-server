package org.layer.domain.retrospect.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRetrospect is a Querydsl query type for Retrospect
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRetrospect extends EntityPathBase<Retrospect> {

    private static final long serialVersionUID = -1198422890L;

    public static final QRetrospect retrospect = new QRetrospect("retrospect");

    public final org.layer.domain.common.QBaseTimeEntity _super = new org.layer.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final EnumPath<RetrospectStatus> retrospectStatus = createEnum("retrospectStatus", RetrospectStatus.class);

    public final NumberPath<Long> spaceId = createNumber("spaceId", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRetrospect(String variable) {
        super(Retrospect.class, forVariable(variable));
    }

    public QRetrospect(Path<? extends Retrospect> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRetrospect(PathMetadata metadata) {
        super(Retrospect.class, metadata);
    }

}

