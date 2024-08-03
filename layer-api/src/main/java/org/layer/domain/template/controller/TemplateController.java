package org.layer.domain.template.controller;

import lombok.RequiredArgsConstructor;
import org.layer.domain.template.controller.dto.TemplateDetailInfoResponse;
import org.layer.domain.template.controller.dto.TemplateSimpleInfoResponse;
import org.layer.domain.template.service.TemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/template")
@Controller
public class TemplateController implements TemplateApi {
    private final TemplateService templateService;

    //== 간단 정보 단건 조회 ==//
    @Override
    @GetMapping("/{templateId}/simple-info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TemplateSimpleInfoResponse> getTemplateSimpleInfo(@PathVariable("templateId") Long templateId) {
        TemplateSimpleInfoResponse templateSimpleInfo = templateService.getTemplateSimpleInfo(templateId);
        return new ResponseEntity<>(templateSimpleInfo, HttpStatus.OK);
    }

    //== 상세 정보 단건 조회 ==//
    @Override
    @GetMapping("/{templateId}/detail-info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TemplateDetailInfoResponse> getTemplateDetailInfo(@PathVariable("templateId") Long templateId) {
        TemplateDetailInfoResponse templateDetailInfo = templateService.getTemplateDetailInfo(templateId);
        return new ResponseEntity<>(templateDetailInfo, HttpStatus.OK);
    }


    //== 모든 템플릿 간단 정보 조회 ==//
    @Override
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TemplateSimpleInfoResponse>> getAllTemplates() {
        return new ResponseEntity<>(templateService.getAllTemplates(), HttpStatus.OK);
    }
}
