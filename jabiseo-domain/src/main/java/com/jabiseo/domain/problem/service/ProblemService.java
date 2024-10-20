package com.jabiseo.domain.problem.service;

import com.jabiseo.domain.problem.exception.ProblemBusinessException;
import com.jabiseo.domain.problem.exception.ProblemErrorCode;
import com.jabiseo.domain.problem.domain.Problem;
import com.jabiseo.domain.problem.domain.ProblemRepository;
import com.jabiseo.domain.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.domain.problem.dto.ProblemWithBookmarkSummaryQueryDto;
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
        List<Long> similarProblemIds = similarProblemsProvider.findSimilarProblemIds(problemId, certificateId, SIMILAR_PROBLEM_COUNT);
        return problemRepository.findSummaryByIdsInWithBookmark(memberId, similarProblemIds);
    }

    public List<ProblemWithBookmarkDetailQueryDto> findProblemsById(Long memberId, List<Long> problemIds) {
        return problemRepository.findDetailByIdsInWithBookmark(memberId, problemIds);
    }
}
