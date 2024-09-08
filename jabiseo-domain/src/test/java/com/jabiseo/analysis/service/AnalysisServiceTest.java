package com.jabiseo.analysis.service;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.domain.Learning;
import com.jabiseo.learning.domain.ProblemSolving;
import com.jabiseo.learning.domain.ProblemSolvingRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.problem.domain.Problem;
import fixture.ProblemFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static fixture.CertificateFixture.createCertificate;
import static fixture.LearningFixture.createLearning;
import static fixture.MemberFixture.createMember;
import static fixture.ProblemFixture.createProblem;
import static fixture.ProblemSolvingFixture.createProblemSolving;
import static org.assertj.core.api.Assertions.assertThat;
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

    private Member member;

    private Certificate certificate;

    @BeforeEach
    void setupMemberAndCertificate() {
        Long memberId = 1L;
        Long certificateId = 2L;
        this.member = createMember(memberId);
        this.certificate = createCertificate(certificateId);
    }

    @Test
    @DisplayName("[findVulnerableVector 테스트] 같은 문제를 여러 번 풀었어도, 해당 문제에 대해 OpenSearch 통신은 한 번 한다.")
    void givenDuplicatedProblemId_whenFindVulnerableVector_thenExecuteClientOnce() throws Exception {
        //given
        Long duplicateProblemId = 3L;
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

    @ParameterizedTest
    @CsvSource(value = {"0:true:-1.0", "0:false:1.0", "1:true:-0.5", "364:false:0.002739726", "126:true:-0.007874016"}, delimiter = ':')
    @DisplayName("[findVulnerableVector 가중치 테스트] N일 차이가 날 경우 1/(N+1)의 가중치를 부여한다. 맞은 경우 가중치는 음수이다.")
    void givenDifferentCreatedAt_whenFindVulnerableVector_thenApplyWeight(int dateDifference, boolean isCorrect, double result) throws Exception {
        //given
        Long problemId = 3L;
        Problem problem = createProblem(problemId);
        Long learningId = 4L;
        Learning learning = createLearning(learningId, certificate, LocalDateTime.now().minusDays(dateDifference));
        List<ProblemSolving> problemSolvings = List.of(
                createProblemSolving(5L, member, problem, learning, isCorrect)
        );
        List<Float> problemVector = List.of(1.0f, 2.0f, 3.0f);

        given(problemSolvingRepository.findByMemberAndLearning_CertificateAndLearning_CreatedAtAfter(eq(member), eq(certificate), any()))
                .willReturn(problemSolvings);
        given(vulnerabilityProvider.findVectorsOfProblems(any(), any()))
                .willReturn(Map.of(problemId, problemVector));

        //when
        List<Float> vulnerableVector = sut.findVulnerableVector(member, certificate);

        //then
        IntStream.range(0, problemVector.size())
                .forEach(i -> {
                    float expected = (float) (problemVector.get(i) * result);
                    float actual = vulnerableVector.get(i);
                    assertThat(actual).isEqualTo(expected);
                });
    }

    @Test
    @DisplayName("[findVulnerableVector 테스트] 풀었던 문제들의 가중치를 곱하여 더한 후 반환한다.")
    void givenProblemSolvings_whenCalculateVulnerableVector_thenSumWeightedVectors() throws Exception {
        //given
        List<Long> problemIds = List.of(1L, 2L, 3L);
        List<Problem> problems = problemIds.stream()
                .map(ProblemFixture::createProblem)
                .toList();
        List<Long> learningIds = List.of(4L, 5L, 6L);
        List<Learning> learnings = List.of(
                createLearning(learningIds.get(0), certificate, LocalDateTime.now().minusDays(0)),
                createLearning(learningIds.get(1), certificate, LocalDateTime.now().minusDays(1)),
                createLearning(learningIds.get(2), certificate, LocalDateTime.now().minusDays(2))
        );
        List<ProblemSolving> problemSolvings = List.of(
                createProblemSolving(7L, member, problems.get(0), learnings.get(0), true),
                createProblemSolving(8L, member, problems.get(1), learnings.get(1), false),
                createProblemSolving(9L, member, problems.get(2), learnings.get(2), true)
        );
        Map<Long, List<Float>> problemIdToVector = Map.of(
                problemIds.get(0), List.of(1.0f, 2.0f, 3.0f),
                problemIds.get(1), List.of(4.0f, 5.0f, 6.0f),
                problemIds.get(2), List.of(7.0f, 8.0f, 9.0f)
        );

        given(problemSolvingRepository.findByMemberAndLearning_CertificateAndLearning_CreatedAtAfter(eq(member), eq(certificate), any()))
                .willReturn(problemSolvings);
        given(vulnerabilityProvider.findVectorsOfProblems(any(), any()))
                .willReturn(
                        Map.of(problemIds.get(0), problemIdToVector.get(problemIds.get(0)),
                                problemIds.get(1), problemIdToVector.get(problemIds.get(1)),
                                problemIds.get(2), problemIdToVector.get(problemIds.get(2))
                        )
                );

        //when
        List<Float> vulnerableVector = sut.findVulnerableVector(member, certificate);

        //then
        List<Float> expected = List.of(-1.0f + 1.0f - 1.3333333f, -2.0f + 2.5f + -2.6666667f, -3.0f + 3.0f - 3.0f);
        assertThat(vulnerableVector).isEqualTo(expected);
    }
}
