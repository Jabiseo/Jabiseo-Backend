package com.jabiseo.problem.dto;

import com.jabiseo.problem.domain.Problem;

import java.util.List;

public record FindBookmarkedProblemsResponse(
        long totalCount,
        long totalPage,
        List<ProblemsResponse> problems
) {
    public static FindBookmarkedProblemsResponse of(long totalCount, long totalPage, List<Problem> problems) {
        return new FindBookmarkedProblemsResponse(
                totalCount,
                totalPage,
                problems.stream()
                        .map(ProblemsResponse::from)
                        .toList()
        );
    }
}
