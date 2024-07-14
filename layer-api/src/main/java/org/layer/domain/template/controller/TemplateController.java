package org.layer.domain.template.controller;

import lombok.RequiredArgsConstructor;
import org.layer.domain.template.api.TemplateApi;
import org.layer.domain.template.dto.*;
import org.layer.domain.template.service.TemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/template")
@Controller
public class TemplateController implements TemplateApi {
    private final TemplateService templateService;

    //== 간단 정보 단건 조회 ==//
    @Override
    @GetMapping("/{templateId}/simple-info")
    public ResponseEntity<TemplateSimpleInfoResponse> getTemplateSimpleInfo(@PathVariable("templateId") Long templateId) {
        TemplateSimpleInfoResponse templateSimpleInfo = templateService.getTemplateSimpleInfo(templateId);
        return new ResponseEntity<>(templateSimpleInfo, HttpStatus.OK);
    }

    //== 상세 정보 단건 조회 ==//
    @Override
    @GetMapping("/{templateId}/detail-info")
    public ResponseEntity<TemplateDetailInfoResponse> getTemplateDetailInfo(@PathVariable("templateId") Long templateId) {
        TemplateDetailInfoResponse templateDetailInfo = templateService.getTemplateDetailInfo(templateId);
        return new ResponseEntity<>(templateDetailInfo, HttpStatus.OK);
    }

    //== 질문을 포함한 간단 정보 ==//
    @Override
    @GetMapping("/{templateId}/question-list")
    public ResponseEntity<TemplateQuestionsResponse> getTemplateQuestionList(@PathVariable Long templateId) {
        TemplateQuestionsResponse templateQuestions = templateService.getTemplateQuestions(templateId);
        return new ResponseEntity<>(templateQuestions, HttpStatus.OK);
    }


    //== 모든 템플릿 ==//
    @Override
    @GetMapping("/all")
    public ResponseEntity<TemplateListResponse> getAllTemplates(AllTemplatesRequest allTemplatesRequest) {
        return new ResponseEntity<>(templateService.getAllTemplates(allTemplatesRequest.page(), allTemplatesRequest.size()), HttpStatus.OK);
    }
}
