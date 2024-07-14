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

    @Override
    @GetMapping("{templateId}/simple-info")
    public ResponseEntity<TemplateSimpleInfoResponse> getTemplateSimpleInfo(@PathVariable("templateId") Long templateId) {
        TemplateSimpleInfoResponse templateSimpleInfo = templateService.getTemplateSimpleInfo(templateId);
        return new ResponseEntity<>(templateSimpleInfo, HttpStatus.OK);
    }

    @Override
    @GetMapping("{templateId}/detail-info")
    public ResponseEntity<TemplateDetailInfoResponse> getTemplateDetailInfo(@PathVariable("templateId") Long templateId) {
        TemplateDetailInfoResponse templateDetailInfo = templateService.getTemplateDetailInfo(templateId);
        return new ResponseEntity<>(templateDetailInfo, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TemplateQuestionsResponse> getTemplateQuestionList(@PathVariable Long templateId) {
        return null;
    }


    @Override
    public ResponseEntity<TemplateListResponse> getAllTemplates() {
        return null;
    }
}
