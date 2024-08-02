package org.layer.domain.form.service;

import java.util.List;
import java.util.Random;

import org.layer.domain.form.controller.dto.request.RecommendFormQueryDto;
import org.layer.domain.form.controller.dto.response.FormGetResponse;
import org.layer.domain.form.controller.dto.response.QuestionGetResponse;
import org.layer.domain.form.controller.dto.response.RecommendFormResponseDto;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.entity.FormType;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.tag.entity.Tag;
import org.layer.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormService {
	private final FormRepository formRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;
	private final QuestionRepository questionRepository;
	private final TagRepository tagRepository;

	private static final int MIN = 10000;
	private static final int MAX = 10002;

	public FormGetResponse getForm(Long formId, Long memberId) {
		Form form = formRepository.findByIdOrThrow(formId);
		List<Tag> tags = tagRepository.findAllByFormId(formId);

		// 해당 스페이스 팀원인지 검증
		if (form.getFormType().equals(FormType.CUSTOM)) {
			Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(form.getSpaceId()));
			team.validateTeamMembership(memberId);
		}

		List<Question> questions = questionRepository.findAllByFormId(formId);

		List<QuestionGetResponse> questionResponses = questions.stream()
			.map(question -> QuestionGetResponse.of(question.getContent(),
				question.getQuestionType().getStyle()))
			.toList();

		return FormGetResponse.of(form.getTitle(), tags, questionResponses);
	}

	public RecommendFormResponseDto getRecommendTemplate(RecommendFormQueryDto queryDto, Long memberId) {
		Random random = new Random();
		Long formId = random.nextLong(MAX - MIN + 1) + MIN;

		Form form = formRepository.findByIdOrThrow(formId);

		List<Tag> tags = tagRepository.findAllByFormId(form.getId());

		// TODO: 템플릿 이미지 필요

		return RecommendFormResponseDto.of(form, tags);
	}

}
