package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.FindProblemsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.ProblemFixture.createProblem;
import static fixture.SubjectFixture.createSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
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
    @DisplayName("시험 조건이 있는 문제 세트 조회를 성공한다.")
    void givenIdsIncludeExamIdAndCount_whenFindingProblems_then() {
        //given
        String certificateId = "1";
        String[] subjectIds = {"2", "3"};
        String examId = "4";
        String[] problemIds = {"5", "6", "7"};
        int count = 4;
        Certificate certificate = createCertificate(certificateId);
        Subject subject1 = createSubject(subjectIds[0], certificate);
        Subject subject2 = createSubject(subjectIds[1], certificate);
        Exam exam = createExam(examId, certificate);
        List<Problem> problems = List.of(
                createProblem(problemIds[0], certificate, exam, subject1),
                createProblem(problemIds[1], certificate, exam, subject2),
                createProblem(problemIds[2], certificate, exam, subject1)
        );
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));
        given(problemRepository.findRandomByExamIdAndSubjectId(examId, subjectIds[0], count))
                .willReturn(List.of(problems.get(0), problems.get(2)));
        given(problemRepository.findRandomByExamIdAndSubjectId(examId, subjectIds[1], count))
                .willReturn(List.of(problems.get(1)));

        //when
        FindProblemsResponse result = sut.execute(certificateId, List.of(subjectIds), Optional.of(examId), count);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().get(0).problemId()).isEqualTo(problemIds[0]);
        assertThat(result.problems().get(1).problemId()).isEqualTo(problemIds[2]);
        assertThat(result.problems().get(2).problemId()).isEqualTo(problemIds[1]);
    }

    @Test
    @DisplayName("시험 조건이 없는 문제 세트 조회를 성공한다.")
    void givenIdsExcludeExamIdAndCount_whenFindingProblems_then() {
        //given
        String certificateId = "1";
        String[] subjectIds = {"2", "3"};
        String[] examIds = {"4", "8"};
        String[] problemIds = {"5", "6", "7"};
        int count = 4;
        Certificate certificate = createCertificate(certificateId);
        Subject subject1 = createSubject(subjectIds[0], certificate);
        Subject subject2 = createSubject(subjectIds[1], certificate);
        Exam exam1 = createExam(examIds[0], certificate);
        Exam exam2 = createExam(examIds[1], certificate);
        List<Problem> problems = List.of(
                createProblem(problemIds[0], certificate, exam1, subject1),
                createProblem(problemIds[1], certificate, exam2, subject2),
                createProblem(problemIds[2], certificate, exam2, subject1)
        );
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));
        given(problemRepository.findRandomBySubjectId(subjectIds[0], count))
                .willReturn(List.of(problems.get(0), problems.get(2)));
        given(problemRepository.findRandomBySubjectId(subjectIds[1], count))
                .willReturn(List.of(problems.get(1)));

        //when
        FindProblemsResponse result = sut.execute(certificateId, List.of(subjectIds), Optional.empty(), count);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().get(0).problemId()).isEqualTo(problemIds[0]);
        assertThat(result.problems().get(1).problemId()).isEqualTo(problemIds[2]);
        assertThat(result.problems().get(2).problemId()).isEqualTo(problemIds[1]);
    }

    @Test
    @DisplayName("문제 세트 조회 시 자격증이 존재하지 않는 경우 예외처리한다.")
    void givenInvalidCertificate_whenFindingProblems_thenReturnError() {
        //given
        String certificateId = "1";
        String subjectId = "2";
        String examId = "3";
        int count = 4;
        given(certificateRepository.findById(anyString())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> sut.execute(certificateId, List.of(subjectId), Optional.of(examId), count))
                .isInstanceOf(CertificateBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", CertificateErrorCode.CERTIFICATE_NOT_FOUND);
    }

    @Test
    @DisplayName("문제 세트 조회 시 자격증에 과목들이 하나라도 매칭되지 않는 경우 예외처리한다.")
    void givenNotMatchingCertificateIdAndSubjectId_whenFindingProblems_thenReturnError() {
        //given
        String[] certificateIds = {"1", "8"};
        String[] subjectIds = {"2", "3"};
        String examId = "4";
        int count = 4;
        Certificate certificate1 = createCertificate(certificateIds[0]);
        Certificate certificate2 = createCertificate(certificateIds[1]);
        createExam(examId, certificate1);
        createSubject(subjectIds[0], certificate1);
        createSubject(subjectIds[1], certificate2);
        given(certificateRepository.findById(certificateIds[0])).willReturn(Optional.of(certificate1));


        //when & then
        assertThatThrownBy(() -> sut.execute(certificateIds[0], List.of(subjectIds), Optional.of(examId), count))
                .isInstanceOf(CertificateBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", CertificateErrorCode.SUBJECT_NOT_FOUND_IN_CERTIFICATE);
    }

    @Test
    @DisplayName("문제 세트 조회 시 자격증에 시험이 매칭되지 않는 경우 예외처리한다.")
    void givenNotMatchingCertificateIdAndExamId_whenFindingProblems_thenReturnError() {
        //given
        String[] certificateIds = {"1", "8"};
        String subjectId = "2";
        String examId = "4";
        int count = 4;
        Certificate certificate1 = createCertificate(certificateIds[0]);
        Certificate certificate2 = createCertificate(certificateIds[1]);
        createExam(examId, certificate2);
        createSubject(subjectId, certificate1);

        given(certificateRepository.findById(certificateIds[0])).willReturn(Optional.of(certificate1));


        //when & then
        assertThatThrownBy(() -> sut.execute(certificateIds[0], List.of(subjectId), Optional.of(examId), count))
                .isInstanceOf(CertificateBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", CertificateErrorCode.EXAM_NOT_FOUND_IN_CERTIFICATE);
    }

}
