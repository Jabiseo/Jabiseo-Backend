package com.jabiseo.problem.application.usecase;

import com.jabiseo.opensearch.SimilarProblemsProvider;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.FindSimilarProblemResponse;
import com.jabiseo.problem.dto.ProblemWithBookmarkSummaryQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindSimilarProblemsUseCase {

    private static final int SIMILAR_PROBLEM_SIZE = 3;

    private final ProblemRepository problemRepository;

    private final SimilarProblemsProvider similarProblemsProvider;

    public List<FindSimilarProblemResponse> execute(Long memberId, Long problemId) {

        List<Long> similarProblemIds = similarProblemsProvider.getSimilarProblems(problemId, SIMILAR_PROBLEM_SIZE);
        List<ProblemWithBookmarkSummaryQueryDto> dtos = problemRepository.findSummaryByIdsInWithBookmark(memberId, similarProblemIds);

        return dtos.stream()
                .map(FindSimilarProblemResponse::of)
                .toList();
    }
}
