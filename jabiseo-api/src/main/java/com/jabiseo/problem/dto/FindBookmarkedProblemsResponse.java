package com.jabiseo.problem.dto;

import java.util.List;

public record FindBookmarkedProblemsResponse(
        long totalCount,
        long totalPage,
        List<ProblemsSummaryResponse> problems
) {

    public static FindBookmarkedProblemsResponse of(long totalCount, long totalPage, List<ProblemWithBookmarkSummaryQueryDto> dtos) {
        return new FindBookmarkedProblemsResponse(
                totalCount,
                totalPage,
                dtos.stream()
                        .map(ProblemsSummaryResponse::from)
                        .toList()
        );
    }
}
