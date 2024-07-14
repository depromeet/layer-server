package org.layer.domain.template.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemplate is a Querydsl query type for Template
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemplate extends EntityPathBase<Template> {

    private static final long serialVersionUID = 1710218900L;

    public static final QTemplate template = new QTemplate("template");

    public final org.layer.domain.QBaseEntity _super = new org.layer.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath firstTag = createString("firstTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath secondTag = createString("secondTag");

    public final StringPath templateImageUrl = createString("templateImageUrl");

    public final StringPath templateName = createString("templateName");

    public final StringPath tipDescription = createString("tipDescription");

    public final StringPath tipTitle = createString("tipTitle");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTemplate(String variable) {
        super(Template.class, forVariable(variable));
    }

    public QTemplate(Path<? extends Template> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemplate(PathMetadata metadata) {
        super(Template.class, metadata);
    }

}

