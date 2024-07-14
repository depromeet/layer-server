package org.layer.domain.template.controller;

import lombok.RequiredArgsConstructor;
import org.layer.domain.template.api.TemplateApi;
import org.layer.domain.template.dto.*;
import org.layer.domain.template.service.TemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class TemplateController implements TemplateApi {
    private final TemplateService templateService;

    @Override
    public ResponseEntity<TemplateSimpleInfoResponse> getTemplateSimpleInfo(TemplateInfoRequest templateInfoRequest) {
        TemplateSimpleInfoResponse templateSimpleInfo = templateService.getTemplateSimpleInfo(templateInfoRequest.id());
        return new ResponseEntity<>(templateSimpleInfo, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TemplateDetailInfoResponse> getTemplateDetailInfo(TemplateInfoRequest templateInfoRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TemplateQuestionsResponse> getTemplateQuestionList(TemplateInfoRequest templateInfoRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TemplateListResponse> getAllTemplates() {
        return null;
    }
}
