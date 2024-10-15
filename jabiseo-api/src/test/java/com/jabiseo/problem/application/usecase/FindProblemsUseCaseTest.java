package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.member.domain.Member;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.MemberFixture.createMember;
import static fixture.ProblemFixture.createProblem;
import static fixture.ProblemWithBookmarkDetailQueryDtoFixture.createProblemWithBookmarkDetailQueryDto;
import static fixture.SubjectFixture.createSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("문제 세트 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindProblemsUseCaseTest {

    @InjectMocks
    FindProblemsUseCase sut;

    @Mock
    CertificateRepository certificateRepository;

    @Mock
    ProblemRepository problemRepository;

    @Test
    @DisplayName("로그인 유저가 시험 조건이 있는 문제 세트 조회를 성공한다.")
    void givenLoginMemberWithIdsIncludeExamIdAndCount_whenFindingProblems_thenFindProblems() {
        //given
        Long certificateId = 1L;
        List<Long> subjectIds = List.of(2L, 3L);
        Long examId = 4L;
        List<Long> problemIds = List.of(5L, 6L, 7L);
        Long memberId = 8L;
        int count = 4;

        Certificate certificate = createCertificate(certificateId);
        List<Subject> subjects = subjectIds.stream()
                .map(id -> createSubject(id, certificate))
                .toList();
        Exam exam = createExam(examId, certificate);
        List<Problem> problems = List.of(
                createProblem(problemIds.get(0), certificate, exam, subjects.get(0)),
                createProblem(problemIds.get(1), certificate, exam, subjects.get(1)),
                createProblem(problemIds.get(2), certificate, exam, subjects.get(0))
        );
        Member member = createMember(memberId);
        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = problems.stream()
                .map(problem -> createProblemWithBookmarkDetailQueryDto(problem, false))
                .toList();

        int problemsToFetchCount = 20;
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(memberId, examId, subjectIds.get(0), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(0), problemWithBookmarkDetailQueryDtos.get(2))));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(memberId, examId, subjectIds.get(1), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(1))));

        //when
        FindProblemsResponse result = sut.execute(member.getId(), certificateId, examId, subjectIds, count);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().stream()
                .map(ProblemsDetailResponse::problemId))
                .containsExactlyInAnyOrder(problemIds.get(0), problemIds.get(1), problemIds.get(2));
    }

    @Test
    @DisplayName("로그인 유저가 시험 조건이 없는 문제 세트 조회를 성공한다.")
    void givenLoginMemberWithIdsExcludeExamIdAndCount_whenFindingProblems_thenFindProblems() {
        //given
        Long certificateId = 1L;
        List<Long> subjectIds = List.of(2L, 3L);
        List<Long> examIds = List.of(4L, 8L);
        List<Long> problemIds = List.of(5L, 6L, 7L);
        Long memberId = 8L;
        int count = 4;

        Certificate certificate = createCertificate(certificateId);
        List<Subject> subjects = subjectIds.stream()
                .map(id -> createSubject(id, certificate))
                .toList();
        List<Exam> exams = examIds.stream()
                .map(id -> createExam(id, certificate))
                .toList();
        List<Problem> problems = List.of(
                createProblem(problemIds.get(0), certificate, exams.get(0), subjects.get(0)),
                createProblem(problemIds.get(1), certificate, exams.get(1), subjects.get(1)),
                createProblem(problemIds.get(2), certificate, exams.get(1), subjects.get(0))
        );
        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = problems.stream()
                .map(problem -> createProblemWithBookmarkDetailQueryDto(problem, false))
                .toList();

        int problemsToFetchCount = 20;
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(memberId, null, subjectIds.get(0), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(0), problemWithBookmarkDetailQueryDtos.get(2))));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(memberId, null, subjectIds.get(1), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(1))));

        //when
        FindProblemsResponse result = sut.execute(memberId, certificateId, null, subjectIds, count);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().stream()
                .map(ProblemsDetailResponse::problemId))
                .containsExactlyInAnyOrder(problemIds.get(0), problemIds.get(1), problemIds.get(2));
    }

    @Test
    @DisplayName("비로그인 유저가 시험 조건이 있는 문제 세트 조회를 성공한다.")
    void givenNonLoginMemberWithIdsIncludeExamIdAndCount_whenFindingProblems_thenFindProblems() {
        //given
        Long certificateId = 1L;
        List<Long> subjectIds = List.of(2L, 3L);
        Long examId = 4L;
        List<Long> problemIds = List.of(5L, 6L, 7L);
        int count = 4;

        Certificate certificate = createCertificate(certificateId);
        List<Subject> subjects = subjectIds.stream()
                .map(id -> createSubject(id, certificate))
                .toList();
        Exam exam = createExam(examId, certificate);
        List<Problem> problems = List.of(
                createProblem(problemIds.get(0), certificate, exam, subjects.get(0)),
                createProblem(problemIds.get(1), certificate, exam, subjects.get(1)),
                createProblem(problemIds.get(2), certificate, exam, subjects.get(0))
        );
        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = problems.stream()
                .map(problem -> createProblemWithBookmarkDetailQueryDto(problem, false))
                .toList();

        int problemsToFetchCount = 20;
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(null, examId, subjectIds.get(0), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(0), problemWithBookmarkDetailQueryDtos.get(2))));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(null, examId, subjectIds.get(1), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(1))));

        //when
        FindProblemsResponse result = sut.execute(null, certificateId, examId, subjectIds, count);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().stream()
                .map(ProblemsDetailResponse::problemId))
                .containsExactlyInAnyOrder(problemIds.get(0), problemIds.get(1), problemIds.get(2));
    }

    @Test
    @DisplayName("비로그인 유저가 시험 조건이 없는 문제 세트 조회를 성공한다.")
    void givenNonLoginMemberWithIdsExcludeExamIdAndCount_whenFindingProblems_thenFindProblems() {
        //given
        Long certificateId = 1L;
        List<Long> subjectIds = List.of(2L, 3L);
        List<Long> examIds = List.of(4L, 8L);
        List<Long> problemIds = List.of(5L, 6L, 7L);
        int count = 4;

        Certificate certificate = createCertificate(certificateId);
        List<Subject> subjects = subjectIds.stream()
                .map(id -> createSubject(id, certificate))
                .toList();
        List<Exam> exams = examIds.stream()
                .map(id -> createExam(id, certificate))
                .toList();
        List<Problem> problems = List.of(
                createProblem(problemIds.get(0), certificate, exams.get(0), subjects.get(0)),
                createProblem(problemIds.get(1), certificate, exams.get(1), subjects.get(1)),
                createProblem(problemIds.get(2), certificate, exams.get(1), subjects.get(0))
        );
        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = problems.stream()
                .map(problem -> createProblemWithBookmarkDetailQueryDto(problem, false))
                .toList();

        int problemsToFetchCount = 20;
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(null, null, subjectIds.get(0), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(0), problemWithBookmarkDetailQueryDtos.get(2))));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(null, null, subjectIds.get(1), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(1))));

        //when
        FindProblemsResponse result = sut.execute(null, certificateId, null, subjectIds, count);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().stream()
                .map(ProblemsDetailResponse::problemId))
                .containsExactlyInAnyOrder(problemIds.get(0), problemIds.get(1), problemIds.get(2));
    }

    @Test
    @DisplayName("문제 세트 조회 시 자격증이 존재하지 않는 경우 예외처리한다.")
    void givenInvalidCertificate_whenFindingProblems_thenReturnError() {
        //given
        Long certificateId = 1L;
        Long subjectId = 2L;
        Long examId = 3L;
        Long memberId = 4L;
        int count = 4;

        given(certificateRepository.findById(anyLong())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> sut.execute(memberId, certificateId, examId, List.of(subjectId), count))
                .isInstanceOf(CertificateBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", CertificateErrorCode.CERTIFICATE_NOT_FOUND);
    }

    @Test
    @DisplayName("문제 세트 조회 시 중복된 과목 ID가 요청되는 경우 중복제거 후 결과를 반환한다.")
    void givenDuplicatedSubjectIds_whenFindingProblems_thenFindProblems() {
        //given
        Long certificateId = 1L;
        List<Long> subjectIds = List.of(2L, 3L);
        List<Long> requestSubjectIds = List.of(2L, 3L, 3L, 2L);
        Long examId = 4L;
        List<Long> problemIds = List.of(5L, 6L, 7L);
        int count = 4;

        Certificate certificate = createCertificate(certificateId);
        List<Subject> subjects = subjectIds.stream()
                .map(id -> createSubject(id, certificate))
                .toList();
        Exam exam = createExam(examId, certificate);
        List<Problem> problems = List.of(
                createProblem(problemIds.get(0), certificate, exam, subjects.get(0)),
                createProblem(problemIds.get(1), certificate, exam, subjects.get(1)),
                createProblem(problemIds.get(2), certificate, exam, subjects.get(0))
        );
        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = problems.stream()
                .map(problem -> createProblemWithBookmarkDetailQueryDto(problem, false))
                .toList();

        int problemsToFetchCount = 20;
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(null, examId, subjectIds.get(0), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(0), problemWithBookmarkDetailQueryDtos.get(2))));
        given(problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(null, examId, subjectIds.get(1), problemsToFetchCount))
                .willReturn(new ArrayList<>(List.of(problemWithBookmarkDetailQueryDtos.get(1))));

        //when
        FindProblemsResponse result = sut.execute(null, certificateId, examId, requestSubjectIds, count);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().stream()
                .map(ProblemsDetailResponse::problemId))
                .containsExactlyInAnyOrder(problemIds.get(0), problemIds.get(1), problemIds.get(2));
    }
}
