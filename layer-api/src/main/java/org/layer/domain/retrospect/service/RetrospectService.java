package org.layer.domain.retrospect.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.form.entity.Form;
import org.layer.domain.form.entity.FormType;
import org.layer.domain.form.repository.FormRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.controller.dto.request.QuestionCreateRequest;
import org.layer.domain.retrospect.controller.dto.request.RetrospectCreateRequest;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.retrospect.service.dto.response.RetrospectGetServiceResponse;
import org.layer.domain.retrospect.service.dto.response.RetrospectListGetServiceResponse;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.exception.MemberSpaceRelationException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.tag.entity.Tag;
import org.layer.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.layer.common.exception.MemberSpaceRelationExceptionType.NOT_FOUND_MEMBER_SPACE_RELATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetrospectService {

    private final RetrospectRepository retrospectRepository;
    private final MemberSpaceRelationRepository memberSpaceRelationRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final FormRepository formRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Long createRetrospect(RetrospectCreateRequest request, Long spaceId, Long memberId) {
        // 해당 스페이스 팀원인지 검증
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        Retrospect retrospect = getRetrospect(request, spaceId);
        Retrospect savedRetrospect = retrospectRepository.save(retrospect);

        List<Question> questions = getQuestions(request.questions(), savedRetrospect.getId(), null);
        questionRepository.saveAll(questions);

        // 새로운 폼 생성(수정)인지 확인
        if (request.isNewForm()) {
            // 내 회고 폼에 추가
            Form form = new Form(memberId, spaceId, request.title(), request.introduction(), FormType.CUSTOM);
            Form savedForm = formRepository.save(form);

            List<Question> myQuestions = getQuestions(request.questions(), null, savedForm.getId());
            questionRepository.saveAll(myQuestions);

            List<Tag> newTags = tagRepository.findAllByFormId(request.curFormId()).stream()
                    .map(tag -> new Tag(tag.getTagName(), savedForm.getId(), null)).toList();
            tagRepository.saveAll(newTags);

        }
        return savedRetrospect.getId();
    }

    private Retrospect getRetrospect(RetrospectCreateRequest request, Long spaceId) {
        return Retrospect.builder()
                .spaceId(spaceId)
                .title(request.title())
                .introduction(request.introduction())
                .retrospectStatus(RetrospectStatus.PROCEEDING)
                .deadline(request.deadline())
                .build();
    }

    public RetrospectListGetServiceResponse getRetrospects(Long spaceId, Long memberId) {
        // 해당 스페이스 팀원인지 검증
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        List<Retrospect> retrospects = retrospectRepository.findAllBySpaceId(spaceId);
        List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
        Answers answers = new Answers(answerRepository.findAllByRetrospectIdIn(retrospectIds));

        List<RetrospectGetServiceResponse> retrospectDtos = retrospects.stream()
                .map(r -> RetrospectGetServiceResponse.of(r.getId(), r.getTitle(), r.getIntroduction(),
                        answers.hasRetrospectAnswer(memberId, r.getId()), r.getRetrospectStatus(),
                        answers.getWriteCount(r.getId()), team.getTeamMemberCount(), r.getCreatedAt(), r.getDeadline()))
                .toList();

        return RetrospectListGetServiceResponse.of(retrospects.size(), retrospectDtos);
    }

    private void validateTeamMember(Long request, Long memberId) {
        Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(
                request, memberId);
        if (team.isEmpty()) {
            throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
        }
    }

    private List<Question> getQuestions(List<QuestionCreateRequest> questions, Long savedRetrospectId, Long formId) {
        AtomicInteger index = new AtomicInteger(1);

        return questions.stream()
                .map(question -> Question.builder()
                        .retrospectId(savedRetrospectId)
                        .formId(formId)
                        .content(question.questionContent())
                        .questionOrder(index.getAndIncrement())
                        .questionOwner(QuestionOwner.TEAM)
                        .questionType(QuestionType.stringToEnum(question.questionType()))
                        .build())
                .toList();
    }
}
