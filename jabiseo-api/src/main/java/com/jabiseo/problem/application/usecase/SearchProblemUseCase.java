package com.jabiseo.problem.application.usecase;

import com.jabiseo.problem.dto.SearchProblemResponse;
import com.jabiseo.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchProblemUseCase {

    private final ProblemService problemService;

    public List<SearchProblemResponse> execute(Long memberId, Long certificateId, String query, Double lastScore, Long lastId) {
        return problemService.searchProblem(memberId, certificateId, query, lastScore, lastId).stream()
                .map(SearchProblemResponse::from)
                .toList();
    }
}
