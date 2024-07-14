package org.layer.domain.template.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.template.dto.TemplateDetailInfoResponse;
import org.layer.domain.template.dto.TemplateListResponse;
import org.layer.domain.template.dto.TemplateQuestionsResponse;
import org.layer.domain.template.dto.TemplateSimpleInfoResponse;
import org.layer.domain.template.entity.Template;
import org.layer.domain.template.entity.TemplateQuestion;
import org.layer.domain.template.exception.TemplateException;
import org.layer.domain.template.exception.TemplateExceptionType;
import org.layer.domain.template.repository.TemplateRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TemplateService {
    private final TemplateRepository templateRepository;

    //== 간단 정보 단건 조회 ==//
    public TemplateSimpleInfoResponse getTemplateSimpleInfo(Long templateId) {
        Template template = templateRepository.findById(templateId).orElseThrow(() -> new TemplateException(TemplateExceptionType.TEMPLATE_NOT_FOUND));
        return TemplateSimpleInfoResponse.toResponse(template);
    }

    //== 상세 정보 단건 조회 ==//
    public TemplateDetailInfoResponse getTemplateDetailInfo(Long templateId) {
        Template template = templateRepository.findById(templateId).orElseThrow(() -> new TemplateException(TemplateExceptionType.TEMPLATE_NOT_FOUND));
        return TemplateDetailInfoResponse.toResponse(template);
    }

    //== 질문을 포함한 간단 정보 단건 조회 ==//
    public TemplateQuestionsResponse getTemplateQuestions(Long templateId) {
        Template template = templateRepository.findById(templateId).orElseThrow(() -> new TemplateException(TemplateExceptionType.TEMPLATE_NOT_FOUND));
        List<TemplateQuestion> questionList = templateRepository.findTemaplteQuestionListById(templateId);

        return TemplateQuestionsResponse.toResponse(template, questionList);
    }

    //== 모든 템플릿 리스트 간단 정보 조회 ==//
    public TemplateListResponse getAllTemplates(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Template> templates = templateRepository.findAll(pageable);
        return TemplateListResponse.toResponse(templates);
    }
}