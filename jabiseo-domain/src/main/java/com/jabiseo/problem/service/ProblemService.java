package com.jabiseo.problem.service;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.problem.dto.ProblemWithBookmarkSummaryQueryDto;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private static final int MAX_PROBLEM_COUNT = 20;
    private static final int SIMILAR_PROBLEM_COUNT = 3;

    private final ProblemRepository problemRepository;
    private final SimilarProblemsProvider similarProblemsProvider;

    public Problem getById(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemBusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND));
    }

    public List<ProblemWithBookmarkSummaryQueryDto> findSimilarProblems(Long memberId, Long problemId, Long certificateId) {
        List<Long> similarProblemIds = similarProblemsProvider.findSimilarProblemIds(problemId, certificateId, SIMILAR_PROBLEM_COUNT);
        return problemRepository.findSummaryByIdsInWithBookmark(memberId, similarProblemIds);
    }

    public List<ProblemWithBookmarkDetailQueryDto> findProblemsById(Long memberId, List<Long> problemIds) {
        return problemRepository.findDetailByIdsInWithBookmark(memberId, problemIds);
    }

    public List<ProblemWithBookmarkDetailQueryDto> findProblemsBySubjectId(Long memberId, List<Long> examIds, List<Long> subjectIds, int count) {
        return subjectIds.stream()
                .distinct()
                .flatMap(subjectId -> {
                    List<ProblemWithBookmarkDetailQueryDto> problems =
                            findProblemsByExamIdsAndSubjectId(memberId, examIds, subjectId, count);
                    Collections.shuffle(problems); // 문제 리스트를 랜덤으로 섞음
                    return problems.stream().limit(count);
                })
                .toList();
    }

    public List<ProblemWithBookmarkDetailQueryDto> findProblemsByExamIdAndSubjectIds(Long memberId, Long examId, List<Long> subjectIds, int count) {
        return subjectIds.stream()
                .distinct()
                .flatMap(subjectId -> {
                    List<ProblemWithBookmarkDetailQueryDto> problems =
                            problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(memberId, examId, subjectId, MAX_PROBLEM_COUNT);
                    Collections.shuffle(problems); // 문제 리스트를 랜덤으로 섞음
                    return problems.stream().limit(count);
                })
                .toList();
    }

    private List<ProblemWithBookmarkDetailQueryDto> findProblemsByExamIdsAndSubjectId(Long memberId, List<Long> examIds, Long subjectId, int count) {
        return examIds.stream()
                .flatMap(examId -> problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(memberId, examId, subjectId, MAX_PROBLEM_COUNT)
                        .stream()
                        .limit(count))
                .collect(Collectors.toList());
    }

}
