package com.jabiseo.problem.service;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.problem.dto.ProblemWithBookmarkSummaryQueryDto;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private static final int SIMILAR_PROBLEM_COUNT = 3;

    private final ProblemRepository problemRepository;
    private final SimilarProblemsProvider similarProblemsProvider;

    public Problem getById(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemBusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND));
    }

    public List<ProblemWithBookmarkSummaryQueryDto> findSimilarProblems(Long memberId, Long problemId, Long certificateId) {
        List<Long> similarProblemIds = similarProblemsProvider.findSimilarProblems(problemId, certificateId, SIMILAR_PROBLEM_COUNT);
        return problemRepository.findSummaryByIdsInWithBookmark(memberId, similarProblemIds);
    }

    public List<ProblemWithBookmarkDetailQueryDto> findProblemsById(Long memberId, List<Long> problemIds) {
        return problemRepository.findDetailByIdsInWithBookmark(memberId, problemIds);
    }
}
