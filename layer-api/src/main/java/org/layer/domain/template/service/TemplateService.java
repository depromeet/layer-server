package org.layer.domain.template.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.template.controller.dto.TemplateSimpleInfoResponse;
import org.layer.domain.template.entity.TemplateMetadata;
import org.layer.domain.template.repository.TemplateMetadataRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemplateService {
    private final FormRepository formRepository;
    private final TemplateMetadataRepository templateMetadataRepository;
    //== 간단 정보 단건 조회 ==//
    public TemplateSimpleInfoResponse getTemplateSimpleInfo(Long formId) {
        Form form = formRepository.findByIdOrThrow(formId);
        TemplateMetadata metadata = templateMetadataRepository.findByFormIdOrThrow(formId);
        return TemplateSimpleInfoResponse.toResponse(form, metadata);
    }
//
//    //== 상세 정보 단건 조회 ==//
//    public TemplateDetailInfoResponse getTemplateDetailInfo(Long templateId) {
//        TemplateMetadata template = templateMetadataRepository.findByIdOrThrow(templateId);
//        List<TemplateQuestion> templateQuestionList = templateQuestionRepository.findAllByTemplateId(templateId);
//        return TemplateDetailInfoResponse.toResponse(template, templateQuestionList);
//    }
//
//    //== 질문을 포함한 간단 정보 단건 조회 ==//
//    public TemplateQuestionListResponse getTemplateQuestions(Long templateId) {
//        TemplateMetadata template = templateMetadataRepository.findById(templateId).orElseThrow(() -> new TemplateException(TemplateExceptionType.TEMPLATE_NOT_FOUND));
//        List<TemplateQuestion> questionList = templateQuestionRepository.findAllByTemplateId(templateId);
//
//        return TemplateQuestionListResponse.toResponse(template, questionList);
//    }
//
//    //== 모든 템플릿 리스트 간단 정보 조회 ==//
//    public TemplateListResponse getAllTemplates(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Slice<TemplateMetadata> templates = templateMetadataRepository.findAll(pageable);
//        return TemplateListResponse.toResponse(templates);
//    }
}