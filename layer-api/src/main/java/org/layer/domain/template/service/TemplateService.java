package org.layer.domain.template.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.template.controller.dto.TemplateDetailInfoResponse;
import org.layer.domain.template.controller.dto.TemplateDetailQuestionResponse;
import org.layer.domain.template.controller.dto.TemplatePurposeResponse;
import org.layer.domain.template.controller.dto.TemplateSimpleInfoResponse;
import org.layer.domain.template.entity.QuestionDescription;
import org.layer.domain.template.entity.TemplateMetadata;
import org.layer.domain.template.entity.TemplatePurpose;
import org.layer.domain.template.repository.QuestionDescriptionRepository;
import org.layer.domain.template.repository.TemplateMetadataRepository;
import org.layer.domain.template.repository.TemplatePurposeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.layer.domain.form.entity.FormType.TEMPLATE;

@Slf4j
@RequiredArgsConstructor
@Service
public class TemplateService {
    private final FormRepository formRepository;
    private final TemplateMetadataRepository templateMetadataRepository;
    private final QuestionRepository questionRepository;
    private final QuestionDescriptionRepository questionDescriptionRepository;
    private final TemplatePurposeRepository templatePurposeRepository;

    //== 간단 정보 단건 조회 ==//
    public TemplateSimpleInfoResponse getTemplateSimpleInfo(Long formId) {
        Form form = formRepository.findByIdOrThrow(formId);
        TemplateMetadata metadata = templateMetadataRepository.findByFormIdOrThrow(formId);
        return TemplateSimpleInfoResponse.toResponse(form, metadata);
    }

    //== 상세 정보 단건 조회 ==//
    public TemplateDetailInfoResponse getTemplateDetailInfo(Long formId) {
        Form form = formRepository.findByIdOrThrow(formId);
        Optional<TemplateMetadata> template = templateMetadataRepository.findByFormId(formId);
        List<Question> questionList = questionRepository.findAllByFormId(formId);

        List<TemplatePurpose> templatePurposeList = templatePurposeRepository.findAllByFormId(formId);

        List<TemplatePurposeResponse> templatePurposeResponses = templatePurposeList.stream().map(TemplatePurposeResponse::toResponse).toList();
        List<TemplateDetailQuestionResponse> questionDesList = questionList.stream().map(q -> {
            Optional<QuestionDescription> description = questionDescriptionRepository.findByQuestionId(q.getId());
            return TemplateDetailQuestionResponse.builder()
                    .questionId(q.getId())
                    .question(q.getContent())
                    .description(description.map(QuestionDescription::getDescription).orElse(null)).build();
        }).toList();

        return TemplateDetailInfoResponse.toResponse(form, template, questionDesList, templatePurposeResponses);
    }


    //== 모든 템플릿 리스트 간단 정보 조회 ==//
    public List<TemplateSimpleInfoResponse> getAllTemplates() {
        List<Form> forms = formRepository.findByFormTypeOrderById(TEMPLATE);

        return forms.stream().map(form -> {
            TemplateMetadata metadata = templateMetadataRepository.findByFormIdOrThrow(form.getId());
            return TemplateSimpleInfoResponse.toResponse(form, metadata);
        }).toList();
    }
}