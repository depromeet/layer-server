package org.layer.domain.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -1184209556L;

    public static final QQuestion question = new QQuestion("question");

    public final org.layer.domain.QBaseEntity _super = new org.layer.domain.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final SetPath<org.layer.domain.questionOption.entity.QuestionOption, org.layer.domain.questionOption.entity.QQuestionOption> options = this.<org.layer.domain.questionOption.entity.QuestionOption, org.layer.domain.questionOption.entity.QQuestionOption>createSet("options", org.layer.domain.questionOption.entity.QuestionOption.class, org.layer.domain.questionOption.entity.QQuestionOption.class, PathInits.DIRECT2);

    public final EnumPath<org.layer.domain.question.enums.QuestionOwner> questionOwner = createEnum("questionOwner", org.layer.domain.question.enums.QuestionOwner.class);

    public final NumberPath<Long> questionOwnerId = createNumber("questionOwnerId", Long.class);

    public final EnumPath<org.layer.domain.question.enums.QuestionType> questionType = createEnum("questionType", org.layer.domain.question.enums.QuestionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQuestion(String variable) {
        super(Question.class, forVariable(variable));
    }

    public QQuestion(Path<? extends Question> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestion(PathMetadata metadata) {
        super(Question.class, metadata);
    }

}

