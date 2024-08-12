package org.layer.domain.form.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.form.controller.dto.request.FormNameUpdateRequest;
import org.layer.domain.form.controller.dto.request.RecommendFormQueryDto;
import org.layer.domain.form.controller.dto.request.RecommendFormSetRequest;
import org.layer.domain.form.controller.dto.response.CustomTemplateListResponse;
import org.layer.domain.form.controller.dto.response.CustomTemplateResponse;
import org.layer.domain.form.controller.dto.response.FormGetResponse;
import org.layer.domain.form.controller.dto.response.QuestionGetResponse;
import org.layer.domain.form.controller.dto.response.RecommendFormResponseDto;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.exception.FormException;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.domain.template.entity.TemplateMetadata;
import org.layer.domain.template.repository.TemplateMetadataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.layer.common.exception.FormExceptionType.UNAUTHORIZED_GET_FORM;
import static org.layer.domain.form.entity.FormType.CUSTOM;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormService {
	private final FormRepository formRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final QuestionRepository questionRepository;
	private final SpaceRepository spaceRepository;
	private final TemplateMetadataRepository metadataRepository;

	private static final int MIN = 10000;
	private static final int MAX = 10002;

	public FormGetResponse getForm(Long formId, Long memberId) {
		Form form = formRepository.findByIdOrThrow(formId);

		// 해당 스페이스 팀원인지 검증
		if (form.getFormType().equals(CUSTOM)) {
			Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(form.getSpaceId()));
			team.validateTeamMembership(memberId);
		}

		List<Question> questions = questionRepository.findAllByFormId(formId);

		List<QuestionGetResponse> questionResponses = questions.stream()
			.map(question -> QuestionGetResponse.of(question.getContent(),
				question.getQuestionType().getStyle()))
			.toList();

		return FormGetResponse.of(form.getTitle(), form.getFormTag().getTag(), questionResponses);
	}

	public RecommendFormResponseDto getRecommendTemplate(RecommendFormQueryDto queryDto, Long memberId) {
		Random random = new Random();
		Long formId = random.nextLong(MAX - MIN + 1) + MIN;

		Form form = formRepository.findByIdOrThrow(formId);
		TemplateMetadata metadata = metadataRepository.findByFormIdOrThrow(formId);
		// TODO: 템플릿 이미지 필요

		return RecommendFormResponseDto.of(form, metadata.getTemplateImageUrl());
	}

	@Transactional
	public void updateFormTitle(Long formId, FormNameUpdateRequest request, Long memberId) {
		Form form = formRepository.findByIdOrThrow(formId);

		validateIsLeader(memberId, form);

		form.updateFormTitle(request.formTitle());
	}

	@Transactional
	public void deleteFormTitle(Long formId, Long memberId) {
		Form form = formRepository.findByIdOrThrow(formId);

		validateIsLeader(memberId, form);

		formRepository.delete(form);
	}

	private void validateIsLeader(Long memberId, Form form) {
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(form.getSpaceId()));
		team.validateTeamMembership(memberId);

		Space space = spaceRepository.findByIdOrThrow(form.getSpaceId());
		space.isLeaderSpace(memberId);
	}

	public CustomTemplateListResponse getCustomTemplateList(Pageable pageable, Long spaceId, Long memberId) {
		// 멤버가 스페이스에 속하는지 검증
		Optional<MemberSpaceRelation> spaceMemberRelation = memberSpaceRelationRepository.findBySpaceIdAndMemberId(spaceId, memberId);
		if(spaceMemberRelation.isEmpty()) {
			throw new FormException(UNAUTHORIZED_GET_FORM);
		}

		Page<Form> customFormList = formRepository.findAllByFormTypeAndSpaceIdOrderByIdDesc(pageable, CUSTOM, spaceId);

		Page<CustomTemplateResponse> customFormResList = customFormList.map(form -> new CustomTemplateResponse(form.getId(), form.getTitle(), form.getFormTag().getTag(), form.getCreatedAt()));

		return CustomTemplateListResponse.builder()
			.customTemplateList(customFormResList)
			.build();
	}

	@Transactional
	public void setRecommendTemplate(RecommendFormSetRequest request, Long memberId) {
		// 팀 소속 여부 검증 로직
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(request.spaceId()));
		team.validateTeamMembership(memberId);

		Form form = formRepository.findByIdOrThrow(request.formId());

		Space space = spaceRepository.findByIdOrThrow(request.spaceId());
		space.updateRecentFormId(form.getId(), memberId);
	}
}
