package org.layer.domain.form.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QForm is a Querydsl query type for Form
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QForm extends EntityPathBase<Form> {

    private static final long serialVersionUID = -927631704L;

    public static final QForm form = new QForm("form");

    public final org.layer.domain.QBaseEntity _super = new org.layer.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath introduction = createString("introduction");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QForm(String variable) {
        super(Form.class, forVariable(variable));
    }

    public QForm(Path<? extends Form> path) {
        super(path.getType(), path.getMetadata());
    }

    public QForm(PathMetadata metadata) {
        super(Form.class, metadata);
    }

}

