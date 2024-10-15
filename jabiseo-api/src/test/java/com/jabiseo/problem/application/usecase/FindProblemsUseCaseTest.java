package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.service.CertificateService;
import com.jabiseo.problem.service.ProblemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.SubjectFixture.createSubject;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("문제 세트 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindProblemsUseCaseTest {

    @InjectMocks
    FindProblemsUseCase sut;

    @Mock
    CertificateService certificateService;

    @Mock
    ProblemService problemService;

    @Test
    @DisplayName("시험 조건이 없는 경우 findProblemsBySubjectId 메소드를 호출한다.")
    void findProblemsBySubjectId() {
        // given
        List<Long> subjectIds = List.of(1L, 2L);
        Long memberId = 1L;
        Long certificateId = 1L;
        List<Long> examIds = List.of(3L, 4L);
        int count = 10;

        Certificate certificate = createCertificate(certificateId);
        examIds.forEach(e -> createExam(e, certificate));
        subjectIds.forEach(s -> createSubject(s, certificate));
        given(certificateService.getById(certificateId)).willReturn(certificate);

        // when
        sut.execute(memberId, certificateId, null, subjectIds, count);

        // then
        verify(problemService).findProblemsBySubjectId(memberId, examIds, subjectIds, count);
    }

    @Test
    @DisplayName("시험 조건이 있는 경우 findProblemsByExamIdAndSubjectIds 메소드를 호출한다.")
    void findProblemsByExamIdAndSubjectId() {
        // given
        List<Long> subjectIds = List.of(1L, 2L);
        Long memberId = 1L;
        Long certificateId = 1L;
        List<Long> examIds = List.of(3L, 4L);
        Long examId = 3L;
        int count = 10;

        Certificate certificate = createCertificate(certificateId);
        examIds.forEach(e -> createExam(e, certificate));
        subjectIds.forEach(s -> createSubject(s, certificate));
        given(certificateService.getById(certificateId)).willReturn(certificate);

        // when
        sut.execute(memberId, certificateId, examId, subjectIds, count);

        // then
        verify(problemService).findProblemsByExamIdAndSubjectIds(memberId, examId, subjectIds, count);
    }

}
