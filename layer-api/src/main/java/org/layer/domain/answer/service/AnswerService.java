package org.layer.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.layer.domain.answer.controller.dto.request.AnswerCreateRequest;
import org.layer.domain.answer.controller.dto.request.AnswerListCreateRequest;
import org.layer.domain.answer.controller.dto.request.AnswerListUpdateRequest;
import org.layer.domain.answer.controller.dto.request.AnswerUpdateRequest;
import org.layer.domain.answer.controller.dto.response.*;
import org.layer.domain.answer.entity.Answer;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.enums.AnswerStatus;
import org.layer.domain.answer.exception.AnswerException;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.common.time.Time;
import org.layer.domain.member.entity.Members;
import org.layer.domain.member.repository.MemberRepository;
import org.layer.domain.question.entity.Question;
import org.layer.domain.question.entity.Questions;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.MemberSpaceRelation;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.exception.MemberSpaceRelationException;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.layer.common.exception.AnswerExceptionType.NOT_ANSWERED;
import static org.layer.common.exception.MemberSpaceRelationExceptionType.NOT_FOUND_MEMBER_SPACE_RELATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final MemberSpaceRelationRepository memberSpaceRelationRepository;
    private final RetrospectRepository retrospectRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    private final Time time;

    @Transactional
    public void create(AnswerListCreateRequest request, Long spaceId, Long retrospectId, Long memberId) {
        // 스페이스 팀원인지 검증
        Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(
                spaceId, memberId);
        if (team.isEmpty()) {
            throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
        }

        // 회고 존재 검증
        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        retrospect.validateDeadline(time.now());

        // 회고 질문 유효성 검사 - 해당 회고에 속해있는 질문인지
        List<Long> questionIds = request.requests().stream()
                .map(AnswerCreateRequest::questionId)
                .toList();
        Questions questions = new Questions(questionRepository.findAllByIdIn(questionIds));
        questions.validateQuestionSize(questionIds.size());

        // 회고 질문 유효성 검사 - 이미 응답을 하지 않았는지
        Answers answers = new Answers(
                answerRepository.findByRetrospectIdAndMemberIdAndAnswerStatusAndQuestionIdIn(retrospectId, memberId,
                        AnswerStatus.DONE, questionIds));
        answers.validateNoAnswer();

        // 기존 임시답변 제거
        answerRepository.deleteAllByRetrospectIdAndMemberIdAndAnswerStatus(retrospectId, memberId,
                AnswerStatus.TEMPORARY);

        AnswerStatus answerStatus = AnswerStatus.DONE;
        if (request.isTemporarySave()) {
            answerStatus = AnswerStatus.TEMPORARY;
        }

        for (AnswerCreateRequest r : request.requests()) {
            // 회고 질문 유효성 검사 - 각각의 질문들이 유효한지
            questions.validateIdAndQuestionType(r.questionId(), QuestionType.stringToEnum(r.questionType()));

            Answer answer = new Answer(retrospectId, r.questionId(), memberId, r.answerContent(), answerStatus);
            answerRepository.save(answer);
        }
    }

    @Transactional
    public void update(AnswerListUpdateRequest request, Long spaceId, Long retrospectId, Long memberId) {
        // 스페이스 팀원인지 검증
        Optional<MemberSpaceRelation> team = memberSpaceRelationRepository.findBySpaceIdAndMemberId(
                spaceId, memberId);
        if (team.isEmpty()) {
            throw new MemberSpaceRelationException(NOT_FOUND_MEMBER_SPACE_RELATION);
        }

        // 회고 존재 검증
        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        retrospect.validateDeadline(time.now());


        // 회고 질문 유효성 검사 - 해당 회고에 속해있는 질문인지
        List<Long> questionIds = request.requests().stream()
                .map(AnswerUpdateRequest::questionId)
                .toList();
        Questions questions = new Questions(questionRepository.findAllByIdIn(questionIds));
        questions.validateQuestionSize(questionIds.size());

        // 회고 질문 유효성 검사 - 모두 완료된 응답인지 확인
        Answers answers = new Answers(
                answerRepository.findByRetrospectIdAndMemberIdAndAnswerStatusAndQuestionIdIn(retrospectId, memberId,
                        AnswerStatus.DONE, questionIds));
        answers.validateContainAnswers();
        for (Answer a : answers.getAnswers()) {
            // 답변에 해당하는 질문이 존재하지 않을 경우 throw
            var foundAnswerRequest = request.requests().stream().filter(it -> it.questionId().equals(a.getQuestionId())).findFirst().orElseThrow(() -> new AnswerException(NOT_ANSWERED));

            a.updateContent(foundAnswerRequest.answerContent());
            answerRepository.save(a);
        }
    }

    public TemporaryAnswerListResponse getTemporaryAnswer(Long spaceId, Long retrospectId, Long memberId) {
        // 해당 스페이스 팀원인지 검증
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        retrospect.isProceedingRetrospect();

        // 임시 답변을 했는지 검증
        Answers answers = new Answers(
                answerRepository.findAllByRetrospectIdAndMemberIdAndAnswerStatus(retrospectId, memberId,
                        AnswerStatus.TEMPORARY));
        answers.validateAlreadyAnswer(memberId, retrospectId);

        // 해당 회고의 모든 질문 조회, 모든 임시답변 조회 -> 질문-임시답변과 매핑
        List<Question> questions = questionRepository.findAllByRetrospectIdOrderByQuestionOrder(retrospectId);

        List<TemporaryAnswerGetResponse> temporaryAnswers = questions.stream()
                .map(question -> TemporaryAnswerGetResponse.of(question.getId(), question.getQuestionType().getStyle(),
                        answers.getAnswerToQuestion(
                                question.getId())))
                .toList();

        return TemporaryAnswerListResponse.of(temporaryAnswers);
    }

    public AnswerListGetResponse getAnalyzeAnswer(Long spaceId, Long retrospectId, Long memberId) {
        // 해당 스페이스 팀원인지 검증
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        retrospect.validateRetrospectStatusDone();

        // answer 뽑기
        Answers answers = new Answers(answerRepository.findAllByRetrospectId(retrospectId));
        List<Long> questionIds = answers.getAnswers().stream().map(Answer::getQuestionId).toList();
        List<Long> memberIds = answers.getAnswers().stream().map(Answer::getMemberId).toList();

        // 이름 뽑기
        Members members = new Members(memberRepository.findAllById(memberIds));

        // Question 뽑기
        List<Question> questions = questionRepository.findAllByIdIn(questionIds);

        // 질문 기준으로 정렬
        List<AnswerByQuestionGetResponse> answerByQuestions = getAnswerByQuestionGetResponses(
                answers, members, questions);

        // 이름 기준으로 정렬
        List<AnswerByPersonGetResponse> answerByPerson = getAnswerByPersonGetResponses(
                answers, members, questions);

        return new AnswerListGetResponse(answerByQuestions, answerByPerson);
    }

    public List<WrittenAnswerGetResponse> getWrittenAnswer(Long spaceId, Long retrospectId, Long memberId) {
        // 해당 스페이스 팀원인지 검증
        Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
        team.validateTeamMembership(memberId);

        Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
        retrospect.isProceedingRetrospect();

        // 완료된 답변 검증
        Answers answers = new Answers(
                answerRepository.findAllByRetrospectIdAndMemberIdAndAnswerStatus(retrospectId, memberId,
                        AnswerStatus.DONE));
        answers.validateAlreadyAnswer(memberId, retrospectId);

        List<Question> questions = questionRepository.findAllByRetrospectIdOrderByQuestionOrder(
                retrospectId);

        return questions.stream()
                .map(question -> WrittenAnswerGetResponse
                        .of(
                                question.getId(),
                                question.getQuestionType().getStyle(),
                                question.getContent(),
                                answers.getAnswerToQuestion(
                                        question.getId())
                        )
                )
                .toList();
    }

    private List<AnswerByPersonGetResponse> getAnswerByPersonGetResponses(Answers answers, Members members,
                                                                          List<Question> questions) {
        return members.getMembers().stream()
                .map(member -> {
                    List<QuestionAndAnswerGetResponse> questionAndAnswer = questions.stream()
                            .map(question -> new QuestionAndAnswerGetResponse(question.getContent(),
                                    question.getQuestionType().getStyle(), answers.getAnswerToQuestion(
                                    question.getId())))
                            .toList();

                    return new AnswerByPersonGetResponse(member.getName(), questionAndAnswer);
                })
                .toList();
    }

    private List<AnswerByQuestionGetResponse> getAnswerByQuestionGetResponses(Answers answers, Members members,
                                                                              List<Question> questions) {
        return questions.stream()
                .map(question -> {
                    List<PersonAndAnswerGetResponse> personAndAnswer = answers.getAnswers().stream()
                            .filter(answer -> answer.getQuestionId().equals(question.getId()))
                            .map(answer -> new PersonAndAnswerGetResponse(members.getName(answer.getMemberId()),
                                    answer.getContent()))
                            .toList();

                    return new AnswerByQuestionGetResponse(question.getContent(), question.getQuestionType().getStyle(),
                            personAndAnswer);
                })
                .toList();
    }
}
