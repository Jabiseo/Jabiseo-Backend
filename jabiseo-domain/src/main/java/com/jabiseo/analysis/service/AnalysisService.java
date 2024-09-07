package com.jabiseo.analysis.service;

import com.jabiseo.analysis.dto.VulnerableSubjectResponse;
import com.jabiseo.analysis.dto.VulnerableTagResponse;
import com.jabiseo.analysis.exception.AnalysisBusinessException;
import com.jabiseo.analysis.exception.AnalysisErrorCode;
import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.domain.ProblemSolving;
import com.jabiseo.learning.domain.ProblemSolvingRepository;
import com.jabiseo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private static final int DEFAULT_TAG_COUNT = 5;
    // 최근 1년간의 학습 데이터만 사용
    private static final int YEARS_OF_ANALYSIS = 1;
    // 추천 모의고사, 취약 문제 조회는 50문제 제공
    private static final int DEFAULT_VULNERABLE_PROBLEM_COUNT = 50;

    private final ProblemSolvingRepository problemSolvingRepository;
    private final VulnerabilityProvider vulnerabilityProvider;

    public List<VulnerableSubjectResponse> findVulnerableSubjects(Member member, Certificate certificate) {
        List<Float> vulnerableVector = findVulnerableVector(member, certificate);
        return vulnerabilityProvider.findVulnerableSubjects(vulnerableVector, certificate.getId());
    }

    public List<VulnerableTagResponse> findVulnerableTags(Member member, Certificate certificate) {
        List<Float> vulnerableVector = findVulnerableVector(member, certificate);
        return vulnerabilityProvider.findVulnerableTags(vulnerableVector, certificate.getId(), DEFAULT_TAG_COUNT);
    }

    public List<Long> findVulnerableProblemIdsOfSubject(Member member, Certificate certificate, Long subjectId) {
        List<Float> vulnerableVector = findVulnerableVector(member, certificate);
        return vulnerabilityProvider.findVulnerableProblemIdsOfSubject(vulnerableVector, certificate.getId(), subjectId, DEFAULT_VULNERABLE_PROBLEM_COUNT);
    }

    public List<Long> findVulnerableProblems(Member member, Certificate certificate) {
        List<Float> vulnerableVector = findVulnerableVector(member, certificate);
        return vulnerabilityProvider.findVulnerableProblems(vulnerableVector, certificate.getId(), DEFAULT_VULNERABLE_PROBLEM_COUNT);
    }

    private List<Float> calculateVulnerableVector(List<ProblemSolving> problemSolvings, Map<Long, List<Float>> problemIdToVector) {
        // 풀었던 문제들의 벡터를 가중치를 곱하여 더한 후 반환
        return problemSolvings.stream()
                .map(problemSolving -> {
                    List<Float> problemVector = problemIdToVector.get(problemSolving.getProblem().getId());
                    double weight = calculateWeight(problemSolving.getLearning().getCreatedAt());
                    return problemVector.stream()
                            .map(value -> {
                                // 문제를 맞췄을 경우 -1을 곱하여 가중치를 적용
                                if (problemSolving.isCorrect())
                                    return (float) (-1 * value * weight);
                                else
                                    return (float) (value * weight);
                            })
                            .toList();
                })
                //.parallel() TODO: 병렬 처리하는 것이 더 효율적인지 테스트 필요
                .reduce((vector1, vector2) ->
                        IntStream.range(0, vector1.size())
                                .mapToObj(i -> vector1.get(i) + vector2.get(i))
                                .toList()
                )
                .orElseThrow(() -> new AnalysisBusinessException(AnalysisErrorCode.CANNOT_CALCULATE_VULNERABILITY));
    }

    private List<Float> findVulnerableVector(Member member, Certificate certificate) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(YEARS_OF_ANALYSIS);
        // TODO: 어떤 쿼리가 더 효율적인지 테스트 필요
        List<ProblemSolving> problemSolvings = problemSolvingRepository.findByMemberAndLearning_CertificateAndLearning_CreatedAtAfter(member, certificate, oneYearAgo);

        List<Long> distinctProblemIds = problemSolvings.stream()
                .map(problemSolving -> problemSolving.getProblem().getId())
                .distinct()
                .toList();

        Map<Long, List<Float>> problemIdToVector = vulnerabilityProvider.findVectorsOfProblems(distinctProblemIds, certificate.getId());
        return calculateVulnerableVector(problemSolvings, problemIdToVector);
    }

    private double calculateWeight(LocalDateTime createdAt) {
        long daysBetween = createdAt.until(LocalDateTime.now(), DAYS);
        // 시간 차이에 반비례한 가중치 계산. N일 차이가 날 경우 1/(N+1)의 가중치를 부여한다.
        return 1.0 / (daysBetween + 1);
    }

}
