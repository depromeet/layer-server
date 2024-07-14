package org.layer.domain.template.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemplateQuestion is a Querydsl query type for TemplateQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemplateQuestion extends EntityPathBase<TemplateQuestion> {

    private static final long serialVersionUID = -719410534L;

    public static final QTemplateQuestion templateQuestion = new QTemplateQuestion("templateQuestion");

    public final StringPath content = createString("content");

    public final StringPath description = createString("description");

    public final StringPath entryWord = createString("entryWord");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<org.layer.domain.question.entity.QuestionType> questionType = createEnum("questionType", org.layer.domain.question.entity.QuestionType.class);

    public final NumberPath<Long> templateId = createNumber("templateId", Long.class);

    public QTemplateQuestion(String variable) {
        super(TemplateQuestion.class, forVariable(variable));
    }

    public QTemplateQuestion(Path<? extends TemplateQuestion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemplateQuestion(PathMetadata metadata) {
        super(TemplateQuestion.class, metadata);
    }

}

