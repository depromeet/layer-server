package org.layer.domain.template.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.template.dto.TemplateSimpleInfoResponse;
import org.layer.domain.template.entity.Template;
import org.layer.domain.template.exception.TemplateException;
import org.layer.domain.template.exception.TemplateExceptionType;
import org.layer.domain.template.repository.TemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}
