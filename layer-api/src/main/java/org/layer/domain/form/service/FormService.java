package org.layer.domain.form.service;

import java.util.List;

import org.layer.domain.form.controller.dto.response.DefaultFormGetResponse;
import org.layer.domain.form.controller.dto.response.DefaultQuestionGetResponse;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
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

	public DefaultFormGetResponse getForm(Long formId, Long memberId){
		Form form = formRepository.findByIdOrThrow(formId);

		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(form.getSpaceId()));
		team.validateTeamMembership(memberId);

		List<Question> questions = questionRepository.findAllByFormId(formId);

		List<DefaultQuestionGetResponse> questionResponses = questions.stream()
			.map(question -> DefaultQuestionGetResponse.of(question.getContent(),
				question.getQuestionType().getStyle()))
			.toList();

		return DefaultFormGetResponse.of(form.getTitle(), questionResponses);
	}

}
