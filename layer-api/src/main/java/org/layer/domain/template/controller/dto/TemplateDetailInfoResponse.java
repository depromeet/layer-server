//package org.layer.domain.template.controller.dto;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.constraints.NotNull;
//import lombok.Builder;
//import org.layer.domain.template.entity.TemplateMetadata;
//import org.layer.domain.template.entity.TemplateQuestion;
//import org.layer.domain.template.exception.TemplateException;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.layer.domain.template.exception.TemplateExceptionType.INVALID_TEMPLATE;
//
//@Builder
//@Schema
//public record TemplateDetailInfoResponse(
//        @Schema(description = "템플릿 ID", example = "1")
//        @NotNull
//        Long id,
//        @Schema(description = "템플릿 제목", example = "빠르고 효율적인 회고")
//        @NotNull
//        String title, // ex. 빠르고 효율적인 회고
//        @Schema(description = "템플릿 명칭", example = "KPT 회고")
//        @NotNull
//        String templateName, // ex. KPT 회고
//        @Schema(description = "템플릿 대표 사진", example = "[이미지 url]")
//        @NotNull
//        String templateImageUrl,
//        @Schema(description = "템플릿 관련 태그 1", example = "간단한 프로세스")
//        String firstTag, // 첫번째 태그. ex) 간단한 프로세스
//        @Schema(description = "템플릿 관련 태그 2", example = "다음 목표 설정")
//        String secondTag, // 두번째 태그. ex) 다음 목표 설정
//        //== 회고 설명에 대한 부분 ==//
//        @Schema(description = "회고에 대한 설명", example = "회고 내용을 Keep, Problem, Try 세가지 관점으로 분류하여... [생략]")
//        @NotNull
//        String description, // 회고에 대한 설명
//        @Schema(description = "회고 팁 제목", example = "회고는 빠르고 간단하게!")
//        @NotNull
//        String tipTitle, // ex) 회고는 빠르고 간단하게!
//        @Schema(description = "회고 팁 설명", example = "KPT 회고는 짧은 시간에 구성원의 생각을... [생략]")
//        @NotNull
//        String tipDescription, // 팁에 대한 설명
//
//        @Schema(description = "템플릿 질문과 그 설명 리스트")
//        @NotNull
//        List<TemplateDetailQuestionResponse> templateDetailQuestionList // 질문(회고 과정)에 대한 설명
//
//) {
//        public static TemplateDetailInfoResponse toResponse(TemplateMetadata template, List<TemplateQuestion> templateQuestionList) {
//                List<TemplateDetailQuestionResponse> templateDetailQuestionList = templateQuestionList
//                        .stream()
//                        .map(TemplateDetailQuestionResponse::toResponse)
//                        .toList();
//
//                return Optional.ofNullable(template)
//                        .map(it -> TemplateDetailInfoResponse.builder()
//                                .id(it.getId())
//                                .title(it.getTitle())
//                                .templateName(it.getTemplateName())
//                                .templateImageUrl(it.getTemplateImageUrl())
//                                .firstTag(it.getFirstTag())
//                                .secondTag(it.getSecondTag())
//                                .description(it.getDescription())
//                                .tipTitle(it.getTipTitle())
//                                .tipDescription(it.getTipDescription())
//                                .templateDetailQuestionList(templateDetailQuestionList)
//                                .build())
//                                .orElseThrow(() -> new TemplateException(INVALID_TEMPLATE));
//
//        }
//}
