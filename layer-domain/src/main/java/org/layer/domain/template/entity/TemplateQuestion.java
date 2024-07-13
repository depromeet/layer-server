package org.layer.domain.template.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.layer.domain.question.converter.QuestionTypeConverter;
import org.layer.domain.question.entity.QuestionType;

@Entity
public class TemplateQuestion {
    @NotNull
    @Id
    private Long id;
    @NotNull
    private Long templateId; // fk: 어떤 템플릿에 속하는지

    @NotNull
    private String content; // 질문 내용

    @NotNull
    @Convert(converter = QuestionTypeConverter.class)
    private QuestionType questionType; // 질문 타입

    //== 회고 템플릿 설명과 관련==//
    private String entryWord; // 표제어. ex) Keep, Problem, Try
    private String description; // 질문에 대한 설명. ex) 현재 만족하고 있거나 계속 이어갔으면 하는 부분들을 작성해요.
}
