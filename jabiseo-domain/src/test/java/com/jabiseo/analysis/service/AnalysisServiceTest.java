package com.jabiseo.analysis.service;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.domain.ProblemSolving;
import com.jabiseo.learning.domain.ProblemSolvingRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.problem.domain.Problem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static fixture.CertificateFixture.createCertificate;
import static fixture.MemberFixture.createMember;
import static fixture.ProblemFixture.createProblem;
import static fixture.ProblemSolvingFixture.createProblemSolving;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("분석 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @InjectMocks
    AnalysisService sut;

    @Mock
    ProblemSolvingRepository problemSolvingRepository;

    @Mock
    VulnerabilityProvider vulnerabilityProvider;

    @Test
    @DisplayName("[findVulnerableVector 테스트] 같은 문제를 여러 번 풀었어도, 해당 문제에 대해 OpenSearch 통신은 한 번 한다.")
    void givenDuplicatedProblemId_whenFindVulnerableVector_thenExecuteClientOnce() throws Exception {
        //given
        Long memberId = 1L;
        Long certificateId = 2L;
        Long duplicateProblemId = 3L;
        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        Problem duplicateProblem = createProblem(duplicateProblemId);
        List<ProblemSolving> problemSolvings = List.of(
                createProblemSolving(3L, member, duplicateProblem),
                createProblemSolving(4L, member, duplicateProblem),
                createProblemSolving(5L, member, duplicateProblem)
        );

        given(problemSolvingRepository.findByMemberAndLearning_CertificateAndLearning_CreatedAtAfter(eq(member), eq(certificate), any()))
                .willReturn(problemSolvings);
        given(vulnerabilityProvider.findVectorsOfProblems(any(), any()))
                .willReturn(Map.of(duplicateProblemId, List.of(1.0f, 2.0f, 3.0f)));

        //when
        sut.findVulnerableVector(member, certificate);

        //then
        verify(vulnerabilityProvider, times(1)).findVectorsOfProblems(any(), any());
    }

}
