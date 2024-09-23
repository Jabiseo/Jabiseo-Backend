package com.jabiseo.analysis.service;

import com.jabiseo.analysis.domain.ProblemSolvingAnalysisType;
import com.jabiseo.analysis.dto.VulnerableSubjectDto;
import com.jabiseo.analysis.dto.VulnerableTagDto;
import com.jabiseo.analysis.exception.AnalysisBusinessException;
import com.jabiseo.analysis.exception.AnalysisErrorCode;
import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.learning.domain.ProblemSolving;
import com.jabiseo.learning.domain.ProblemSolvingRepository;
import com.jabiseo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.LocalDateTime.now;

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

    public List<VulnerableSubjectDto> findVulnerableSubjects(Member member, Certificate certificate) {
        List<Float> vulnerableVector = findVulnerableVector(member, certificate);
        return vulnerabilityProvider.findVulnerableSubjects(vulnerableVector, certificate.getId());
    }

    public List<VulnerableTagDto> findVulnerableTags(Member member, Certificate certificate) {
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

    List<Float> findVulnerableVector(Member member, Certificate certificate) {

        ProblemSolvingAnalysisType longestPeriodAnalysisType = ProblemSolvingAnalysisType.getLongestPeriodAnalysisType();
        LocalDateTime fromDate = now().minusDays(longestPeriodAnalysisType.getMaxPeriodDay());
        Pageable pageable = Pageable.ofSize(longestPeriodAnalysisType.getMaxCount());

        List<ProblemSolving> longestTermProblemSolvings = problemSolvingRepository.findWithLearningByCreatedAtAfterOrderByCreatedAtDesc(member, certificate, fromDate, pageable);

        if (longestTermProblemSolvings.isEmpty()) {
            throw new AnalysisBusinessException(AnalysisErrorCode.NOT_ENOUGH_SOLVED_PROBLEMS);
        }

        List<Long> distinctProblemIds = longestTermProblemSolvings.stream()
                .map(problemSolving -> problemSolving.getProblem().getId())
                .distinct()
                .toList();

        Map<Long, List<Float>> problemIdToVector = vulnerabilityProvider.findVectorsOfProblems(distinctProblemIds, certificate.getId());
        Map<Long, Double> problemIdToWeight = calculateWeightsOfProblems(longestTermProblemSolvings);
        return calculateVulnerableVector(distinctProblemIds, problemIdToVector, problemIdToWeight);
    }

    Map<Long, Double> calculateWeightsOfProblems(List<ProblemSolving> problemSolvings) {
        return IntStream.range(0, problemSolvings.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> problemSolvings.get(i).getProblem().getId(),
                        i -> calculateWeight(problemSolvings.get(i), i),
                        Double::sum
                ));
    }

    List<Float> calculateVulnerableVector(List<Long> distinctProblemIds, Map<Long, List<Float>> problemIdToVector, Map<Long, Double> problemIdToWeight) {
        return distinctProblemIds.stream()
                .map(problemId -> {
                    List<Float> vector = problemIdToVector.get(problemId);
                    double weight = problemIdToWeight.get(problemId);
                    return vector.stream()
                            .map(value -> (float) (value * weight))
                            .toList();
                })
                .reduce((vector1, vector2) -> IntStream.range(0, vector1.size())
                        .mapToObj(i -> vector1.get(i) + vector2.get(i))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new AnalysisBusinessException(AnalysisErrorCode.NOT_ENOUGH_SOLVED_PROBLEMS));
    }

    // problemSolving과 그 문제를 최근에 접한 순서를 주면 가중치를 반환한다. sequence는 최근 푼 문제 순서
    double calculateWeight(ProblemSolving problemSolving, int sequence) {
        int daysAgo = (int) ChronoUnit.DAYS.between(problemSolving.getLearning().getCreatedAt(), now());
        ProblemSolvingAnalysisType analysisType = ProblemSolvingAnalysisType.fromPeriodAndCount(daysAgo, sequence);
        return problemSolving.isCorrect() ? -analysisType.getWeight() : analysisType.getWeight();
    }

}
