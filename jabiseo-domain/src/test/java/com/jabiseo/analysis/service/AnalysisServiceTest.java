package com.jabiseo.analysis.service;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.domain.Learning;
import com.jabiseo.learning.domain.ProblemSolving;
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

@DisplayName("분석 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @InjectMocks
    AnalysisService sut;

    private Member member;

    private Certificate certificate;

    @BeforeEach
    void setupMemberAndCertificate() {
        Long memberId = 1L;
        Long certificateId = 2L;
        this.member = createMember(memberId);
        this.certificate = createCertificate(certificateId);
    }

    @ParameterizedTest
    @CsvSource(value = {"0:true:-1.0", "0:false:1.0", "1:true:-0.5", "364:false:0.002739726", "126:true:-0.007874016"}, delimiter = ':')
    @DisplayName("[calculateVulnerableVector 가중치 테스트] N일 차이가 날 경우 1/(N+1)의 가중치를 부여한다. 맞은 경우 가중치는 음수이다.")
    void givenDifferentCreatedAt_whenFindVulnerableVector_thenApplyWeight(int dateDifference, boolean isCorrect, double result) {
        //given
        Long problemId = 3L;
        Problem problem = createProblem(problemId);
        Long learningId = 4L;
        Learning learning = createLearning(learningId, certificate, LocalDateTime.now().minusDays(dateDifference));
        List<ProblemSolving> problemSolvings = List.of(
                createProblemSolving(5L, member, problem, learning, isCorrect)
        );
        List<Float> problemVector = List.of(1.0f, 2.0f, 3.0f);
        Map<Long, List<Float>> problemIdToVector = Map.of(problemId, problemVector);

        //when
        List<Float> vulnerableVector = sut.calculateVulnerableVector(problemSolvings, problemIdToVector);

        //then
        IntStream.range(0, problemVector.size())
                .forEach(i -> {
                    float expected = (float) (problemVector.get(i) * result);
                    float actual = vulnerableVector.get(i);
                    assertThat(actual).isEqualTo(expected);
                });
    }

    @Test
    @DisplayName("[calculateVulnerableVector 테스트] 풀었던 문제들의 가중치를 곱하여 더한 후 반환한다.")
    void givenProblemSolvings_whenCalculateVulnerableVector_thenSumWeightedVectors() {
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

        //when
        List<Float> vulnerableVector = sut.calculateVulnerableVector(problemSolvings, problemIdToVector);

        //then
        List<Float> expected = List.of(-1.0f + 1.0f - 1.3333333f, -2.0f + 2.5f - 2.6666667f, -3.0f + 3.0f - 3.0f);
        assertThat(vulnerableVector).isEqualTo(expected);
    }
}
