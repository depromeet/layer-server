package org.layer.domain.space.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpace is a Querydsl query type for Space
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpace extends EntityPathBase<Space> {

    private static final long serialVersionUID = 866940678L;

    public static final QSpace space = new QSpace("space");

    public final org.layer.domain.QBaseEntity _super = new org.layer.domain.QBaseEntity(this);

    public final EnumPath<SpaceCategory> category = createEnum("category", SpaceCategory.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<SpaceField> field = createEnum("field", SpaceField.class);

    public final NumberPath<Long> formId = createNumber("formId", Long.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath introduction = createString("introduction");

    public final NumberPath<Long> leaderId = createNumber("leaderId", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSpace(String variable) {
        super(Space.class, forVariable(variable));
    }

    public QSpace(Path<? extends Space> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpace(PathMetadata metadata) {
        super(Space.class, metadata);
    }

}

